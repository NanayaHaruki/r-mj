package com.nanaya.r_mj

import org.junit.Test

import org.junit.Assert.*
import java.util.TreeMap
import java.util.TreeSet
import kotlin.math.min

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class Solution {
    fun maxScore(grid: List<List<Int>>): Int {
        // 对每个数，跳到这个数的最大得分，就是在左上方矩形内找最小的数做差，跳几步无所谓
        // 统计所有子矩形的最小值，顺便求差
        val m = grid.size
        val n = grid[0].size
        val f = Array(m){IntArray(n)}
        var ans = Int.MIN_VALUE
        for(i in 0 until m){
            for(j in 0 until n){
                val x = grid[i][j]
                if(i==0 && j==0) {
                    f[0][0]=x
                    continue
                }
                val mn = when{
                    i==0 -> f[0][j-1]
                    j==0 -> f[i-1][j]
                    else -> min(f[i-1][j],f[i][j-1])
                }
                val diff = x-mn
                if(diff>ans)
                    ans = diff
                f[i][j]=min(x,mn)
            }
        }
        return ans
    }
}

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val ans = Solution().maxScore(
            listOf(
                listOf(9,5,7,3), listOf(8,9,6,1),
                listOf(6,7,14,3), listOf(2,5,3,1)))
        print(ans)
    }
}