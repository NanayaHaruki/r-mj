package com.nanaya.r_mj

import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

const val mod = 1000000007

class Solution {
    fun maxStrength(nums: IntArray): Long {
        val n= nums.size
        if(n==1) return nums[0].toLong()
        nums.sort()
        var l=0
        var r = n-1
        var p = 1
        var zeroIndex = nums.binarySearch(0)
        if(zeroIndex<0) zeroIndex=-zeroIndex-1
        var ans = 1L
        for (i in 1 until zeroIndex step 2){
            ans *= nums[i - 1] * nums[i]
        }
        for(i in zeroIndex until n){
            if(nums[zeroIndex]==0) continue
            ans *= nums[i]
        }
        return ans
    }
}

class ExampleUnitTest {

    @Test
    fun test() {
       val local = LocalDateTime.now()
        val p = DateTimeFormatter.ofPattern("y-m-d")
        println(local.format(p))
    }
}