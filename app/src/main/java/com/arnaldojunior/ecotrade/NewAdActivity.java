package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.arnaldojunior.ecotrade.databinding.ActivityNewAdBinding;
import com.arnaldojunior.ecotrade.view.CategoryFragment;
import com.google.android.material.button.MaterialButton;

public class NewAdActivity extends AppCompatActivity implements CategoryFragment.OnListFragmentInteractionListener {

    private ActivityNewAdBinding binding;
    private CategoryFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_ad);

        setSupportActionBar(binding.newAdToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.newAdContent.finalidadeDoar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton((MaterialButton) v);
                deselectButton(binding.newAdContent.finalidadeVender);
                binding.newAdContent.newAdValor.setVisibility(View.INVISIBLE);
            }
        });

        binding.newAdContent.finalidadeVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton((MaterialButton) v);
                deselectButton(binding.newAdContent.finalidadeDoar);
                binding.newAdContent.newAdValor.setVisibility(View.VISIBLE);
            }
        });

        binding.newAdContent.categoriaTextInput.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategorySelectionFragment();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(String item) {
        binding.newAdContent.categoriaTextInput.getEditText().setText(item.toString());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void openCategorySelectionFragment() {
        fragment = new CategoryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.newAdContent, fragment);
        transaction.commit();
    }

    public void selectButton(MaterialButton button) {
        button.setBackgroundColor(getResources().getColor(R.color.button_option));
        button.setTextColor(getResources().getColor(R.color.white));
    }

    public void deselectButton(MaterialButton button) {
        button.setBackgroundColor(getResources().getColor(R.color.white));
        button.setTextColor(getResources().getColor(R.color.button_option));
    }
}
