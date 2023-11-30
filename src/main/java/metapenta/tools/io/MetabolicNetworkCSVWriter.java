package metapenta.tools.io;

import metapenta.model.MetabolicNetwork;

import java.io.IOException;

public class MetabolicNetworkCSVWriter implements Writer {

    private MetabolicNetwork metabolicNetwork;

    private String fileName;


    public MetabolicNetworkCSVWriter(MetabolicNetwork metabolicNetwork, String fileName){
        this.metabolicNetwork = metabolicNetwork;
        this.fileName = fileName;

    }
    @Override
    public void write() throws IOException {

    }



}
