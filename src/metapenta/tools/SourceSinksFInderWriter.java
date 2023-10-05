package metapenta.tools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.nio.file.Files;

import metapenta.petrinet2.Place;

public class SourceSinksFInderWriter {
	private StringBuilder sourcesBuilder = new StringBuilder();
    private StringBuilder sinksBuilder = new StringBuilder();
    
    private String prefix;
    private String sourcesFileName;
    private String sinksFileName;
    private StringUtils stringUtils;
    
    public SourceSinksFInderWriter(String prefix){
        this.prefix = prefix;
        this.sourcesFileName= prefix + "_sourcesFinder.txt";
        this.sinksFileName = prefix + "_sinksFinder.txt";
        this.stringUtils = new StringUtils();
    }
    
    public void WriteInMetabolitesSource(String metabolite){
        stringUtils
                .SetString(metabolite)
                .addSingleQuotes()
                .addEmptySpace()
                .addBreakLine();

        sourcesBuilder.append(stringUtils.GetString());
    }
    
    public void writeSources(List<Place<?>>  places){
//        for(String metaboliteId: ids){
//         WriteInMetabolites(metaboliteId);
//        }
        for (int i = 0; i < places.size(); i++) {
        	Place<?> place = places.get(i);
        	String id = place.getID();
        	WriteInMetabolitesSource(id);
			
		}   
        
    }
    
    public void Write() throws Exception{
        Files.write(Paths.get(sourcesFileName), sourcesBuilder.toString().getBytes());
        //Files.write(Paths.get(reactionsFile), reactionsBuilder.toString().getBytes());
    }


    
    
    
}
