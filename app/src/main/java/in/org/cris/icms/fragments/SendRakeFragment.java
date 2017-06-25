package in.org.cris.icms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.RakeAdapter;
import in.org.cris.icms.models.Rake;
import in.org.cris.icms.models.Train;

public class SendRakeFragment extends Fragment {
    private List<Rake> rakeList = new ArrayList<>();
    private int[] trainNumbers = {64466, 64422, 64076, 64463, 22403, 12801, 12397, 64074, 64421, 64003, 12426, 12391, 12206, 12565, 12002};
    private String[] trainNames = {"PNP NDLS MEMU", "NDLS GZB EMU", "NDLS PALWAL EMU", "NDLS KKDE MEMU", "PDY NDLS EXPRESS", "PURUSHOTTAM EXP", "MAHABODHI EXP", "NDLS KSV EMU", "GZB NDLS EMU", "NDLS SNP EMU", "JAMMU RAJDHANI", "SHRAMJEEVI EXP", "NANDA DEVI EXP", "BIHAR S KRANTI", "BHOPAL SHTBDI"};
    private static final int SEND = 0;

    public SendRakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_send_rake, container, false);
        RecyclerView sendRakeRecyclerView = (RecyclerView)view.findViewById(R.id.send_rake_recycler_view);
        RakeAdapter adapter = new RakeAdapter(rakeList, getActivity(), SEND);

        for (int i=0; i<trainNumbers.length; i++){
            Rake rake = new Rake();
            rake.setTrainNo(trainNumbers[i]);
            rake.setTrainName(trainNames[i]);
            rake.setDate("23/06/2017");
            rakeList.add(rake);
        }

        sendRakeRecyclerView.setAdapter(adapter);
        sendRakeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

}
