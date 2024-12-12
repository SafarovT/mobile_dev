package com.example.cookieclicker

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookieclicker.databinding.FragmentCookieBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat

class CookieFragment : Fragment(R.layout.fragment_cookie) {
    private lateinit var binding: FragmentCookieBinding
    private val viewModel by lazy {
        val provider = ViewModelProvider(owner = this)
        provider[ViewModel::class.java]
    }
    private lateinit var mainHandler: Handler
    private val updateCookies = object : Runnable {
        override fun run() {
            viewModel.onChange()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCookieBinding.bind(view)
        adapter = ProductAdapter {
            viewModel.shopPurchase(it)
        }
        binding.shop.listView.adapter = adapter
        binding.shop.listView.layoutManager = LinearLayoutManager(this.context)

        viewModel.state
            .onEach { updateData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event
            .onEach { handleEvent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateCookies)
        initEventListeners()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    private fun updateData(state: State) {
        binding.clicker.count.text = resources.getString(R.string.cookies) + state.cookies.toString()
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        binding.clicker.time.text = resources.getString(R.string.time) + " " + simpleDateFormat.format(state.time * 1000)
        binding.clicker.speed.text = resources.getString(R.string.in_second) + " " + state.cookiesPerSecond.toString()
        binding.clicker.avgSpeed.text = resources.getString(R.string.average_speed) + " " + state.avgSpeed.toString() + " " + resources.getString(R.string.speed_measurement)
        binding.clicker.root.isInvisible = state.tab != Tab.CLICKER
        binding.shop.root.isInvisible = state.tab != Tab.SHOP

        adapter.productsList = state.shopList
        adapter.notifyDataSetChanged()
    }

    private fun initEventListeners() {
        binding.clicker.button.setOnClickListener {
            viewModel.onCookieClick()
            animateCookie(binding.clicker.button)
        }
        binding.navigation.setOnItemSelectedListener {
            viewModel.onTabSelect(it)
            return@setOnItemSelectedListener true
        }
    }

    private fun animateCookie(button: ImageButton) {
        button.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(100)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                button.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun handleEvent(event: CookieEvent) {
        val message = when (event) {
            CookieEvent.NotEnoughToBuy ->
                resources.getString(R.string.shop_not_enough_money_message)

            CookieEvent.CookiesIncomeNotification ->
                resources.getString(R.string.income_message)
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}