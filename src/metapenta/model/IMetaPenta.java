package metapenta.model;

import java.io.IOException;

public interface IMetaPenta {
     void describeMetabolicNetwork(String outFilePrefix) throws Exception;
     void getSinks(String outFilePrefix) throws Exception;
     
     void sourcesFinder(String outFilePrefix) throws Exception;
     
}
