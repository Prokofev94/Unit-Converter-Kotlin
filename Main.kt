package converter

const val KILOMETER_TO_METER = 1000.0
const val CENTIMETER_TO_METER = 0.01
const val MILLIMETER_TO_METER = 0.001
const val MILE_TO_METER = 1609.35
const val YARD_TO_METER = 0.9144
const val FOOT_TO_METER = 0.3048
const val INCH_TO_METER = 0.0254
const val KILOGRAM_TO_GRAM = 1000.0
const val MILLIGRAM_TO_GRAM = 0.001
const val POUND_TO_GRAM = 453.592
const val OUNCE_TO_GRAM = 28.3495

val weight = listOf("gram", "kilogram", "milligram", "pound", "ounce")
val length = listOf("meter", "kilometer", "centimeter", "millimeter", "mile", "yard", "foot", "inch")
val temperature = listOf("degree Celsius", "degree Fahrenheit", "kelvin")

fun main() {
    while (true) {
        println("Enter what you want to convert (or exit):")
        val input = readln()
        if (input == "exit") break
        if (!Regex("(?i)-?\\d+(.\\d+)?( degrees?)? \\w+ \\w+( degrees?)? \\w+").matches(input)) {
            println("Parse error")
            continue
        }
        val array = input.split(" ")
        val n = array[0].toDouble()
        val inputUnit = if (Regex("degrees?").matches(array[array.size - 2].lowercase()))
            getUnit(array[array.size - 4].lowercase())
        else
            getUnit(array[array.size - 3].lowercase())
        if (isNegativeValue(n, inputUnit)) continue
        val outputUnit = getUnit(array.last().lowercase())
        if (isNotValid(inputUnit, outputUnit)) continue
        convert(n, inputUnit, outputUnit)
    }
}

fun isNotValid(input: String, output: String): Boolean {
    if (input == "???" || output == "???" || incompatible(listOf(input, output))) {
        println("Conversion from ${inPlural(input)} to ${inPlural(output)} is impossible\n")
        return true
    }
    return false
}

fun incompatible(list: List<String>): Boolean {
    return !(weight.containsAll(list) || length.containsAll(list) || temperature.containsAll(list))
}

fun getUnit(unit: String) = when (unit) {
    "m", "meter", "meters" -> "meter"
    "km", "kilometer", "kilometers" -> "kilometer"
    "cm", "centimeter", "centimeters" -> "centimeter"
    "mm", "millimeter", "millimeters" -> "millimeter"
    "mi", "mile", "miles" -> "mile"
    "yd", "yard", "yards" -> "yard"
    "ft", "foot", "feet" -> "foot"
    "in", "inch", "inches" -> "inch"
    "g", "gram", "grams" -> "gram"
    "kg", "kilogram", "kilograms" -> "kilogram"
    "mg", "milligram", "milligrams" -> "milligram"
    "lb", "pound", "pounds" -> "pound"
    "oz", "ounce", "ounces" -> "ounce"
    "c", "dc", "celsius" -> "degree Celsius"
    "f", "df", "fahrenheit" -> "degree Fahrenheit"
    "k", "kelvin", "kelvins" -> "kelvin"
    else -> "???"
}

fun inPlural(unit: String) = when (unit) {
    "???" -> unit
    "foot" -> "feet"
    "inch" -> unit + "es"
    "degree Celsius" -> "degrees Celsius"
    "degree Fahrenheit" -> "degrees Fahrenheit"
    else -> unit + "s"
}

fun convert(n: Double, inputUnit: String, outputUnit: String) {
    var result = n
    when (inputUnit) {
        in weight -> {
            when (inputUnit) {
                "kilogram" -> result *= KILOGRAM_TO_GRAM
                "milligram" -> result *= MILLIGRAM_TO_GRAM
                "pound" -> result *= POUND_TO_GRAM
                "ounce" -> result *= OUNCE_TO_GRAM
            }
            when (outputUnit) {
                "kilogram" -> result /= KILOGRAM_TO_GRAM
                "milligram" -> result /= MILLIGRAM_TO_GRAM
                "pound" -> result /= POUND_TO_GRAM
                "ounce" -> result /= OUNCE_TO_GRAM
            }
        }
        in length -> {
            when (inputUnit) {
                "kilometer" -> result *= KILOMETER_TO_METER
                "centimeter" -> result *= CENTIMETER_TO_METER
                "millimeter" -> result *= MILLIMETER_TO_METER
                "mile" -> result *= MILE_TO_METER
                "yard" -> result *= YARD_TO_METER
                "foot" -> result *= FOOT_TO_METER
                "inch" -> result *= INCH_TO_METER
            }
            when (outputUnit) {
                "kilometer" -> result /= KILOMETER_TO_METER
                "centimeter" -> result /= CENTIMETER_TO_METER
                "millimeter" -> result /= MILLIMETER_TO_METER
                "mile" -> result /= MILE_TO_METER
                "yard" -> result /= YARD_TO_METER
                "foot" -> result /= FOOT_TO_METER
                "inch" -> result /= INCH_TO_METER
            }
        }
        in temperature -> {
            result = when {
                inputUnit == "degree Celsius" && outputUnit == "degree Fahrenheit" -> n * 9 / 5 + 32
                inputUnit == "degree Celsius" && outputUnit == "kelvin" -> n + 273.15
                inputUnit == "degree Fahrenheit" && outputUnit == "degree Celsius" -> (n - 32) * 5 / 9
                inputUnit == "degree Fahrenheit" && outputUnit == "kelvin" -> (n + 459.67) * 5 / 9
                inputUnit == "kelvin" && outputUnit == "degree Celsius" -> n - 273.15
                inputUnit == "kelvin" && outputUnit == "degree Fahrenheit" -> n * 9 / 5 - 459.67
                else -> result
            }
        }
    }
    val input = if (n == 1.0) inputUnit else inPlural(inputUnit)
    val output = if (result == 1.0) outputUnit else inPlural(outputUnit)
    println("$n $input is $result $output\n")
}

fun isNegativeValue(n: Double, unit: String): Boolean {
    if (unit in length + weight && n < 0) {
        val measure = if (unit in length) "Length" else "Weight"
        println("$measure shouldn't be negative")
        return true
    }
    return false
}