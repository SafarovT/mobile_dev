package com.example.cookieclicker

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Tab {
    CLICKER,
    SHOP,
}

data class State(
    val cookies: Long = 0,
    val avgSpeed: Int = 1,
    val cookiesPerSecond: Long = 0,
    val time: Long = 0,
    val shopList: List<Product> = productsList,
    val tab: Tab = Tab.CLICKER,
)

class ViewModel: ViewModel() {
    val state = MutableStateFlow(State())

    fun onChange() {

        state.update {
            val newCookies = it.cookies + it.cookiesPerSecond
            it.copy(
                cookies = newCookies,
                time = it.time + 1,
                shopList = it.shopList.map {
                    it.copy(disabled = newCookies < it.price)
                }
            )
        }
    }

    fun onCookieClick() {
        state.update { it.copy(cookies = it.cookies + 1) }
    }

    fun onTabSelect(item: MenuItem) {
        state.update { it.copy(
            tab =
                if (item.title == "Clicker")
                    Tab.CLICKER
                else
                    Tab.SHOP,
        ) }
    }

    fun shopPurchase(product: Product) {
        viewModelScope.launch {
            if (state.value.cookies < product.price) {
                return@launch
            }

            state.update { prev ->
                prev.copy(
                    cookies = prev.cookies - product.price,
                    cookiesPerSecond = (prev.cookiesPerSecond + product.income).toLong(),
                    shopList = prev.shopList.map {
                        if (it.titleId === product.titleId) {
                            it.copy(
                                count = it.count + 1,
                            )
                        }
                        else it
                    }
                )
            }
        }
    }
}

val productsList = listOf(
    Product(
        resId   = R.drawable.cursor,
        titleId = R.string.cursor,
        price   = 15,
        income  = 1,
    ),
    Product(
        resId   = R.drawable.grandmother,
        titleId = R.string.grandmother,
        price   = 100,
        income  = 4,
    ),
    Product(
        resId   = R.drawable.farm,
        titleId = R.string.farm,
        price   = 1100,
        income  = 40,
    ),
    Product(
        resId   = R.drawable.mine,
        titleId = R.string.mine,
        price   = 12000,
        income  = 500,
    ),
    Product(
        resId   = R.drawable.fabric,
        titleId = R.string.fabric,
        price   = 130000,
        income  = 1000,
    ),
    Product(
        resId   = R.drawable.bank,
        titleId = R.string.bank,
        price   = 1400000,
        income  = 5000,
    ),
    Product(
        resId   = R.drawable.temple,
        titleId = R.string.temple,
        price   = 20000000,
        income  = 15000,
    ),
    Product(
        resId   = R.drawable.wizard_tower,
        titleId = R.string.wizard_tower,
        price   = 330000000,
        income  = 100000,
    )
)