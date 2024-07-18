package com.example.mystoryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mystoryapp.data.api.ApiConfigUser
import com.example.mystoryapp.data.api.RemoteDataSourceUser
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.databinding.FragmentRegisterBinding
import com.example.mystoryapp.repositories.UserRepository
import com.example.mystoryapp.viewModels.UserViewModel
import com.example.mystoryapp.viewModels.UserViewModelFactory
import com.example.mystoryapp.utils.Result

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels{
        UserViewModelFactory(UserRepository.getInstance(RemoteDataSourceUser(ApiConfigUser), UserPreference(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        userViewModel.registerResponseData.observe(viewLifecycleOwner){
            if(it != null){
                when(it) {
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Register Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireContext() as AppCompatActivity).supportActionBar?.hide()

        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when{
                email.isEmpty() or password.isEmpty() or name.isEmpty() ->{
                    binding.edRegisterEmail.requestFocus()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.validation_empty_field),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                !email.matches(Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")) -> {
                    binding.edRegisterEmail.requestFocus()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.validation_email_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                password.length < 8 -> {
                    binding.edRegisterPassword.requestFocus()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.validation_password_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    userViewModel.registerUser(name, email, password)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}