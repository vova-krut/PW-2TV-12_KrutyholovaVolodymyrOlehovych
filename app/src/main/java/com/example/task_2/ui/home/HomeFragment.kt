package com.example.task_2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task_2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCalculateTask1.setOnClickListener { task1() }

        return root
    }

    private fun round(num: Double) = "%.2f".format(num).toDouble()

    private val fuelStorage: Map<String, Map<String, Double>> = mapOf(
        "coal" to mapOf(
            "qri" to 20.47,
            "avin" to 0.8,
            "ar" to 25.20,
            "gvin" to 1.5
        ),
        "fuel_oil" to mapOf(
            "qri" to 40.40,
            "avin" to 1.0,
            "ar" to 0.15,
            "gvin" to 0.0
        ),
        "natural_gas" to mapOf(
            "qri" to 33.08,
            "avin" to 1.25,
            "ar" to 0.0,
            "gvin" to 0.0
        )
    )

    private fun calculateEmission(fuelId: String, value: Double): List<Double> {
        val fuelData = fuelStorage[fuelId] ?: error("Fuel ID not found")

        val qri = fuelData["qri"] ?: error("QRI not found")
        val avin = fuelData["avin"] ?: error("Avin not found")
        val ar = fuelData["ar"] ?: error("AR not found")
        val gvin = fuelData["gvin"] ?: error("Gvin not found")

        val ktv = (1e6 / qri) * avin * (ar / (100 - gvin)) * (1 - 0.985)
        val etv = 1e-6 * ktv * qri * value

        return listOf(ktv, etv).map { round(it) }
    }

    private fun task1()
    {

        val coal = binding.coal.text.toString().toDoubleOrNull()
        val fuelOil = binding.fuelOil.text.toString().toDoubleOrNull()
        val naturalGas = binding.naturalGas.text.toString().toDoubleOrNull()

        var output = ""

        if (coal != null)
        {
            val ktvEtvCoal = calculateEmission("coal", coal)
            output += "Показник емісії твердих частинок при спалюванні вугілля становитиме: ${ktvEtvCoal[0]} г/ГДж\n"
            output += "Валовий викид при спалюванні вугілля становитиме: ${ktvEtvCoal[1]} т\n"
        }

        if (fuelOil != null)
        {
            val ktvEtvFuelOil = calculateEmission("fuel_oil", fuelOil)
            output += "Показник емісії твердих частинок при спалюванні мазуту становитиме: ${ktvEtvFuelOil[0]} г/ГДж\n"
            output += "Валовий викид при спалюванні мазуту становитиме: ${ktvEtvFuelOil[1]} т\n"
        }

        if (naturalGas != null)
        {
            val ktvEtvNaturalGas = calculateEmission("natural_gas", naturalGas)
            output += "Показник емісії твердих частинок при спалюванні природнього газу становитиме: ${ktvEtvNaturalGas[0]} г/ГДж\n"
            output += "Валовий викид при спалюванні природнього газу становитиме: ${ktvEtvNaturalGas[1]} т\n"
        }

        binding.outputTask1.text = output;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}