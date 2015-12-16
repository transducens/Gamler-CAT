/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.ua.dlsi.tests.statystics;

import es.ua.dlsi.segmentation.Segment;
import es.ua.dlsi.translationmemory.TranslationMemory;
import es.ua.dlsi.utils.CmdLineParser;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author miquel
 */
public class ComputeEditionsMeanAndVariance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option otestsource = parser.addStringOption('s',"source");
        CmdLineParser.Option otesttarget = parser.addStringOption('t',"target");
        CmdLineParser.Option ooutput = parser.addStringOption('o',"output");

        try{
            parser.parse(args);
        }
        catch(CmdLineParser.IllegalOptionValueException e){
            System.err.println(e);
            System.exit(-1);
        }
        catch(CmdLineParser.UnknownOptionException e){
            System.err.println(e);
            System.exit(-1);
        }

        String testsource=(String)parser.getOptionValue(otestsource,null);
        String testtarget=(String)parser.getOptionValue(otesttarget,null);
        String output=(String)parser.getOptionValue(ooutput,null);

        if(output==null){
            System.err.println("Error: It is necessary to define the path of the file where the features will be writen (use parameter --feature-output).");
            System.exit(-1);
        }
        if(testsource==null){
            System.err.println("Error: It is needed to define the path to the source language segments of the test set (use parameter --test-source).");
            System.exit(-1);
        }
        if(testtarget==null){
            System.err.println("Error: It is needed to define the path to the target language segments of the test set (use parameter --test-source).");
            System.exit(-1);
        }
        List<Segment> stestsegs=new LinkedList<Segment>();
        try {
            stestsegs=TranslationMemory.ReadSegmentsFile(testsource);
        } catch (FileNotFoundException ex) {
            System.err.print("Error: Source language test segments file '");
            System.err.print(testsource);
            System.err.println("' could not be found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.print("Error while reading source language test segments from file '");
            System.err.print(testsource);
            System.err.println("'");
            System.exit(-1);
        }

        List<Segment> ttestsegs=new LinkedList<Segment>();
        try {
            ttestsegs=TranslationMemory.ReadSegmentsFile(testtarget);
        } catch (FileNotFoundException ex) {
            System.err.print("Error: Target language test segments file '");
            System.err.print(testsource);
            System.err.println("' could not be found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.print("Error while reading target language test segments from file '");
            System.err.print(testsource);
            System.err.println("'");
            System.exit(-1);
        }

        if(stestsegs.size() != ttestsegs.size()){
            System.err.println("Error: files chosen contain a different number of segments");
            System.exit(-1);
        }

        PrintWriter pw;
        try{
            //pw=new PrintWriter(output);
            pw=new PrintWriter(new GZIPOutputStream(new FileOutputStream(output)));
        } catch(FileNotFoundException ex){
            System.err.println("Warning: Output file "+output+" could not be found: the results will be printed in the default output.");
            pw=new PrintWriter(System.out);
        } catch(IOException ex){
            System.err.println("Warning: Error while writting file "+output+": the results will be printed in the default output.");
            pw=new PrintWriter(System.out);
        }

        /*for(int i=0; i<stestsegs.size(); i++){
            for(TranslationUnit tu: trans_memory.GetTUs()){
                if(fms){
                    double sscore=TranslationMemory.GetScore(stestsegs.get(i), tu.getSource(), threshold, null);
                    double tscore=TranslationMemory.GetScore(ttestsegs.get(i), tu.getTarget(), 0, null);
                    if(sscore>=threshold)
                        pw.println(sscore+"\t"+tscore);
                }else{
                    int seditions=Segment.EditDistance(stestsegs.get(i).getSentenceCodes(),
                            tu.getSource().getSentenceCodes(), null, null, false);
                    int teditions=Segment.EditDistance(ttestsegs.get(i).getSentenceCodes(),
                            tu.getTarget().getSentenceCodes(), null, null, false);
                    pw.println(seditions+"\t"+teditions);
                }
            }
        }*/

        pw.close();
    }

}
