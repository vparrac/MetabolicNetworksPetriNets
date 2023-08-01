package metapenta.tools;

import metapenta.commands.GapFIll;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GapFillWriter {
    private StringBuilder metabolitesBuilder = new StringBuilder();
    private StringBuilder reactionsBuilder = new StringBuilder();
    private StringBuilder sMatrixBuilder = new StringBuilder();
    private StringBuilder eMetabolitesFileBuilder = new StringBuilder();
    private StringBuilder cMetabolitesFileBuilder = new StringBuilder();
    private StringBuilder reversibleRxnBuilder = new StringBuilder();
    private StringBuilder upBoundFileBuilder = new StringBuilder();
    private StringBuilder loBoundsFileBuilder = new StringBuilder();
    private String prefix;
    private String metabolitesFileName;
    private String reactionsFile;
    private String sMatrixFile;
    private String eMetabolitesFile;
    private String cMetabolitesFile;
    private String reversibleRxnFile;
    private String upBoundFile;
    private String loBoundsFile;

    public GapFillWriter(String prefix){
        this.prefix = prefix;
        this.metabolitesFileName = prefix + "compounds.txt";
        this.reactionsFile = prefix + "reactions.txt";
        this.sMatrixFile = prefix + "S_matrix.txt";
        this.eMetabolitesFile = prefix + "extracellularMetabolites.txt";
        this.cMetabolitesFile = prefix + "cytosolic_Metabolites.txt";
        this.reversibleRxnFile = prefix + "reversible_rxn.txt";
        this.upBoundFile = prefix + "upperbounds_on_fluxes.txt";
        this.loBoundsFile = prefix + "lowerbounds_on_fluxes.txt";
    }

    public void WriteInMetabolites(String s){
        metabolitesBuilder.append(s);
    }

    public void WriteInReactions(String s){
        reactionsBuilder.append(s);

    }

    public void WriteInSMatrix(String s){
        sMatrixBuilder.append(s);
    }

    public void WriteEMetabolites(String s){
        eMetabolitesFileBuilder.append(s);
    }

    public void WriteCMetabolites(String s){
        cMetabolitesFileBuilder.append(s);
    }

    public void WriteImUpBound(String s){
        upBoundFileBuilder.append(s);
    }

    public void WriteInReversibleReactions(String s){
        reversibleRxnBuilder.append(s);
    }

    public void WriteInLowBound(String s){
        loBoundsFileBuilder.append(s);
    }

    public void Write() throws Exception{
        Files.write(Paths.get(metabolitesFileName), metabolitesBuilder.toString().getBytes());
        Files.write(Paths.get(reactionsFile), reactionsBuilder.toString().getBytes());
        Files.write(Paths.get(sMatrixFile), sMatrixBuilder.toString().getBytes());
        Files.write(Paths.get(eMetabolitesFile), eMetabolitesFileBuilder.toString().getBytes());
        Files.write(Paths.get(cMetabolitesFile), cMetabolitesFileBuilder.toString().getBytes());
        Files.write(Paths.get(reversibleRxnFile), reversibleRxnBuilder.toString().getBytes());
        Files.write(Paths.get(upBoundFile), upBoundFileBuilder.toString().getBytes());
        Files.write(Paths.get(loBoundsFile), loBoundsFileBuilder.toString().getBytes());
    }
}
