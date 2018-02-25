package CoinSubPages;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.evmcstudios.cryptotracker.MainPages.CTCoinDetails;
import com.evmcstudios.cryptotracker.R;
import com.squareup.picasso.Picasso;

import Objects.CoinItem;

/**
 * Created by Ultranova on 2/24/2018.
 */

public class CoinQuantityPage extends Fragment {


    private ImageView mainImage;
    private EditText quantity;
    private Button updatebtn;
    private CoinItem CoinData;


    public CoinQuantityPage() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       CoinData = ((CTCoinDetails) getActivity()).selectedCoin;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.subpage_content_quantity, container, false);

        // set the views

        mainImage = view.findViewById(R.id.coin_image);


        // load image

        quantity = view.findViewById(R.id.quantity);
        updatebtn = view.findViewById(R.id.update_btn);
        quantity.setText(((CTCoinDetails) getActivity()).selectedCoin.getQuantity());



        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CTCoinDetails) getActivity()).updateCoin(quantity);

            }
        });


        Picasso.with(((CTCoinDetails) getActivity()).getApplicationContext()).load(CoinData.getImageUrl()).fit().centerCrop().into(mainImage);



        return view;

    }


}
