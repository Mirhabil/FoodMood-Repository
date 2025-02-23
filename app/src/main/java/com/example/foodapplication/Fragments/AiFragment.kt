package com.example.foodapplication.Fragments

import AiQuestionHandler
import TimeHelper
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.foodapplication.Adapters.AiAdapter
import com.example.foodapplication.databinding.FragmentAiBinding
import com.example.openai.AI.Weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var adapterList = mutableListOf<String>()

class AiFragment : Fragment() {
    lateinit var time: String
    var temperature: Double = 0.0
    lateinit var response: String
    val defaultQuestion = "Give the food suggestion !"

    private lateinit var binding: FragmentAiBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupKeyboardListener()

        lifecycleScope.launch {
            val weatherInfo = WeatherRepository.fetchWeather("Azerbaijan")
            temperature = weatherInfo.main.temp
            Log.d("Temperature", temperature.toString())


            time = TimeHelper.getTimeOfDay()
        }


        val adapter = AiAdapter()
        binding.rv.adapter = adapter


        binding.askButton.setOnClickListener {
            val question = binding.questionInput.text.toString()

            binding.questionInput.setText("")
            binding.rv.scrollToPosition(adapterList.size - 1)

            if (question.isNotEmpty()) {
                binding.lottieAnimationView4.visibility = View.GONE
                adapterList.add(question)
                adapter.updateList(adapterList)
                binding.lottieAnimationView.post {
                    binding.lottieAnimationView.visibility = View.VISIBLE
                    binding.lottieAnimationView.playAnimation()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (question == defaultQuestion) {
                        response =
                            AiQuestionHandler.getAiAnswer("food suggestion in $time time for $temperature temperature")
                    } else {
                        response = AiQuestionHandler.getAiAnswer(question)
                    }

                    adapterList.add(response)
                    adapter.updateList(adapterList)
                    binding.rv.scrollToPosition(adapterList.size - 1)

                    binding.lottieAnimationView.post {
                        binding.lottieAnimationView.visibility = View.GONE
                        binding.lottieAnimationView.cancelAnimation()
                    }
                }

            }

            if (adapterList.size > 0) {
                binding.tvFoodSuggestion.visibility = View.GONE
            }

        }


        binding.tvFoodSuggestion.setOnClickListener {
            binding.tvFoodSuggestion.visibility = View.GONE
            binding.questionInput.setText(defaultQuestion)
        }


        binding.questionInput.showKeyboard()


    }

    private fun setupKeyboardListener() {
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open → Move input field up
                binding.messageInputContainer.translationY = -keypadHeight.toFloat()
            } else {
                // Keyboard is closed → Reset position
                binding.messageInputContainer.translationY = 0f
            }
        }
    }

}

private fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}
