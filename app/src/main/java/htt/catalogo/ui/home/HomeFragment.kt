package htt.catalogo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import htt.catalogo.databinding.FragmentHomeBinding
 import htt.catalogo.R

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Reemplazar el contenedor con el ProductListFragment
        childFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, ProductListFragment())
            .commit()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}