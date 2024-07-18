package com.example.mystoryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mystoryapp.data.api.ApiConfigUser
import com.example.mystoryapp.data.api.RemoteDataSourceUser
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.databinding.FragmentOnBoardingBinding
import com.example.mystoryapp.repositories.UserRepository
import com.example.mystoryapp.viewModels.UserViewModel
import com.example.mystoryapp.viewModels.UserViewModelFactory

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels{
        UserViewModelFactory(UserRepository.getInstance(RemoteDataSourceUser(ApiConfigUser), UserPreference(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root

        userViewModel.userSession().observe(viewLifecycleOwner){
            if (it != null) {
                findNavController().navigate(R.id.action_onBoardingFragment_to_mainFragment)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (requireContext() as AppCompatActivity).supportActionBar?.hide()

        binding.loginButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_onBoardingFragment_to_loginFragment)
        )

        binding.registerButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_onBoardingFragment_to_registerFragment)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}