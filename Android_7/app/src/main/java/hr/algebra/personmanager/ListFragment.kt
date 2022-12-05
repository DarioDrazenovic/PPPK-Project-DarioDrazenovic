package hr.algebra.personmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.personmanager.dao.Person
import hr.algebra.personmanager.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment(), NavigableFragment {

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPeople()
        setupListeners()

    }

    private fun loadPeople() {
        GlobalScope.launch(Dispatchers.Main) { // dispatch in MAIN thread
            val people = withContext(Dispatchers.IO) { // work in IO thread
                (context?.applicationContext as App).getPersonDao().getPeople()
            }
            binding.rvPeople.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PersonAdapter(requireContext(), people, this@ListFragment)
            }
        }
    }

    private fun setupListeners() {
        binding.fbEdit.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_EditFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigate(bundle: Bundle) {
        findNavController().navigate(R.id.action_ListFragment_to_EditFragment, bundle)
    }
}