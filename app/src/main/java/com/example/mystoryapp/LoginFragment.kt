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
import com.example.mystoryapp.databinding.FragmentLoginBinding
import com.example.mystoryapp.repositories.UserRepository
import com.example.mystoryapp.viewModels.UserViewModel
import com.example.mystoryapp.viewModels.UserViewModelFactory
import com.example.mystoryapp.utils.Result

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels{
        UserViewModelFactory(UserRepository.getInstance(RemoteDataSourceUser(ApiConfigUser), UserPreference(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        userViewModel.loginResponseData.observe(viewLifecycleOwner) {
            if (it != null){
                when(it){
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Login Error", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

        userViewModel.userSession().observe(viewLifecycleOwner){
            if (it != null) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (requireContext() as AppCompatActivity).supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when{
                email.isEmpty() or password.isEmpty()->{
                    binding.edLoginEmail.requestFocus()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.validation_empty_field),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    userViewModel.loginUser(email, password)
                }
            }
        }
    }
}