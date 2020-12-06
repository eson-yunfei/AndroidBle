package com.shon.bluetooth

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)


        val  mutableList:MutableList<String> = mutableListOf("zzz","ddd","aaa")


        val map = mutableList.map {
           getString(it)
        }


        println("find  $map")

//        val find = mutableList.find {
//            it == "bbb"
//        }
//
//
//        println("find  $find")


    }

    private fun  getString(string: String) :String{
        if (string == "zzz") {
          return  "1111"
        }
        return  string
    }



}