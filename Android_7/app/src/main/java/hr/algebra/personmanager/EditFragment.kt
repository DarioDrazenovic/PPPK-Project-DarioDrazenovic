package hr.algebra.personmanager

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import hr.algebra.personmanager.dao.Person
import hr.algebra.personmanager.databinding.FragmentEditBinding
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

const val PERSON_ID = "hr.algebra.personmanager.person_id"
private const val IMAGE_TYPE = "image/*"

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null

    private lateinit var person: Person

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPerson()
        setupListeners()
/*
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_EditFragment_to_ListFragment)
        }

 */
    }

    private fun fetchPerson() {
        val personId = arguments?.getLong(PERSON_ID)
        if (personId != null) {
            GlobalScope.launch(Dispatchers.Main) {

                person = withContext(Dispatchers.IO) {
                    (context?.applicationContext as App).getPersonDao().getPerson(personId) ?: Person()
                }
                bindPerson()
            }

        } else {
            person = Person()
            bindPerson()
        }

    }

    private fun bindPerson() {
        binding.etFirstName.setText(person.firstName ?: "")
        binding.etLastName.setText(person.lastName ?: "")
        binding.tvDate.text = person.birthDate.format(DateTimeFormatter.ISO_DATE)
        if (person.picturePath != null) {
            Picasso.get()
                .load(File(person.picturePath))
                .transform(RoundedCornersTransformation(50, 5))
                .into(binding.ivImage)
        } else {
            binding.ivImage.setImageResource(R.mipmap.ic_launcher)
        }
    }

    private fun setupListeners() {
        binding.tvDate.setOnClickListener {
            handleDate()
        }
        binding.ivImage.setOnLongClickListener {
            handleImage()
            true
        }
        binding.btnCommit.setOnClickListener {
            if (formValid()) {
                commit()
            }
        }
        binding.etFirstName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                person.firstName = text?.toString()?.trim()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.etLastName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                person.lastName = text?.toString()?.trim()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun handleDate() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                person.birthDate = LocalDate.of(year, month + 1, dayOfMonth)
                bindPerson()
            },
            person.birthDate.year,
            person.birthDate.monthValue - 1,
            person.birthDate.dayOfMonth
        ).show()
    }

    private fun handleImage() {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = IMAGE_TYPE
            imageResult.launch(this)
        }

    }

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                if (person.picturePath != null) {
                    File(person.picturePath).delete()
                }
                val dir = context?.applicationContext?.getExternalFilesDir(null)
                val file = File(dir, File.separator.toString() + UUID.randomUUID().toString() + ".jpg")
                context?.contentResolver?.openInputStream(it.data?.data as Uri).use { inputStream ->
                    FileOutputStream(file).use { fos ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val bos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        fos.write(bos.toByteArray())
                        person.picturePath = file.absolutePath
                        bindPerson()
                    }
                }
            }

        }

    private fun formValid(): Boolean {
        var ok = true
        arrayOf(binding.etFirstName, binding.etLastName, binding.etOnlineName).forEach {
            if (it.text.isNullOrEmpty()) {
                it.error = getString(R.string.please)
                ok = false
            }
        }
        return ok && person.picturePath != null
    }

    private fun commit() {
        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {
                if (person._id == null) {
                    (context?.applicationContext as App).getPersonDao().insert(person)
                } else {
                    (context?.applicationContext as App).getPersonDao().update(person)
                }
            }
            findNavController().navigate(R.id.action_EditFragment_to_ListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}