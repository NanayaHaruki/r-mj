package com.nanaya.r_mj

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

const val mod = 1000000007

data class User(
    val age: Int,
    val name: String
)

class Solution {
    class SparseTable(val data: IntArray) {
        // 存的是 log2(i)的值，也就是st[i][j]中的j，表示长度
        // 长度为8，log(8)=3，存在st[i][3], 长度为4567都存在st[i][2]中
        private val log: IntArray = IntArray(data.size + 1)

        // st[i][j] 为以i为开始，长度是2^j的最大值，
        // st[i][3]，是[i,i+8]的最大值
        // st[i][2]，数[i,i+4]的最大值

        private val st: Array<IntArray>

        init {
            val n = data.size
            for (i in 2..n) {
                log[i] = log[i / 2] + 1
            }
            val mxLog = Int.SIZE_BITS - n.countLeadingZeroBits()
            st = Array(n) { IntArray(mxLog) }

            for (i in 0 until n) {
                st[i][0] = data[i]
            }
            for (j in 1 until mxLog) {
                for (i in 0..n - (1 shl j)) {
                    // dp,区间最大值，比对前半段最大值和后半段最大值
                    st[i][j] = max(st[i][j - 1], st[i + (1 shl (j - 1))][j - 1])
                }
            }
        }

        fun query(l: Int, r: Int): Int {
            val j = log[r - l + 1]
            // 如果 l=0,r=6,长度为7，那么j为2，查询长度为4，是[0,3]的最大值,即st[0][2]
            // 0 1 2 3 4 5 6
            // - - - -  st[l][j]只能找前面一半，还需要判断后面一半
            // r-l+1=len, l = r-len+1,找到以r为右端点，长度为2^j的后半段

            // 如果更好长度是2的幂呢
            // 0 1 2 3 4 5 6 7
            // st[l][3] 刚好覆盖了完整区间，而r-(1 shl j)+1 就是l，所谓“前半”和“后半”都是完整的区间，不影响结果
            return max(st[l][j], st[r - (1 shl j) + 1][j])
        }
    }

    fun check(chargeTimes: IntArray, runningCosts: IntArray, budget: Long, k: Int): Boolean {
        var smRunning = 0L
        for (i in 0 until k) {
            smRunning += runningCosts[i]
        }
        if (sparseTable.query(0, k - 1) + k * smRunning <= budget) return true
        for (i in k until chargeTimes.size) {
            smRunning = smRunning + runningCosts[i] - runningCosts[i - k]
            if (sparseTable.query(i - k + 1, i) + k * smRunning <= budget) return true
        }
        return false
    }

    lateinit var sparseTable: SparseTable
    fun maximumRobots(chargeTimes: IntArray, runningCosts: IntArray, budget: Long): Int {
        sparseTable = SparseTable(chargeTimes)
        var l = 1
        val n = chargeTimes.size + 1
        var r = n
        while (l  < r) {
            val k = (l + r) / 2
//            print("$l $r $k")
            if (check(chargeTimes, runningCosts, budget, k)) {
//                println(" true")
                l = k+1
            } else {
//                println(" false")
                r = k
            }
        }
        return l-1
    }
}

class ExampleUnitTest {

    @Test
    fun test() {
        println(floor(-8700.0/1000))

    }
}