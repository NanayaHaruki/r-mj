package com.nanaya.r_mj

import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.lang.Integer.sum
import java.util.TreeMap
import java.util.TreeSet
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.properties.Delegates

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

const val mod = 1000000007

class Solution {

    fun check(ps:IntArray,len: Int,k:Int): Boolean {
        for(i in len until ps.size){
            // 全都改成T 或 全都改成F
            if(ps[i]-ps[i-len]+k>=len || ps[i]-ps[i-len]<=k) return true
        }
        return false
    }

    fun maxConsecutiveAnswers(answerKey: String, k: Int): Int {
        // 二分可能的最大连续长度
        // 根据前缀和+滑动窗口，查找当前二分的答案能否满足要求
        val n = answerKey.length
        val ps = IntArray(n + 1)
        for (i in answerKey.indices) {
            ps[i + 1] = ps[i] + if (answerKey[i] == 'T') 1 else 0
        }
        var l = 0
        var r = answerKey.length + 1
        while (l + 1 < r) {
            val m = (l + r) / 2
            if(check(ps,m,k)){
                l=m
            }else{
                r=m
            }
        }
        return l
    }
}

class ExampleUnitTest {

    @Test
    fun test() {
        val s = ""
        val p = s.split(',')
        println(p.size)
        println(p)
    }
}