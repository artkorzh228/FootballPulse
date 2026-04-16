fun main() {
    val numbers = intArrayOf(1, 2, 3, 4, 5)
    println(runningSum(numbers))

}

fun runningSum(nums: IntArray): IntArray {
    var sum = 0
    val result = IntArray(nums.size)
    for(i in nums.indices) {
        sum += nums[i]
        result[i] = sum
    }
    return result
}