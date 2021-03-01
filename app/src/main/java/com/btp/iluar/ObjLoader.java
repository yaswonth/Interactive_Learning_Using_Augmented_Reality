package com.btp.iluar;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ObjLoader {
    ArrayList<Float> buffer = new ArrayList<Float>();
    ArrayList <Short> indi = new ArrayList <Short> ();
    private final String VERTEX = "v ";
    private final String FACE = "f";
    private final String TEXTURE = "vt";
    private final String NORMAL = "vn";
    private AssetManager asset;
    private BufferedReader fileReader;
    private StringTokenizer scanner;
    private int bs;
    private int ins;


    public ObjLoader(AssetManager assets) throws IOException{
        this.asset = assets;
    }


    public static void searchData(String[] dataval,ArrayList<Float> coorf,String skip,String dty,ArrayList<Short> coors) throws IOException {
        for (String da:dataval){
            if(da.equals(skip)){
                continue;
            }if(dty.equals("float")){
                coorf.add(Float.valueOf(da));
            }else if (dty.equals("short")){
                coors.add((short) (Short.valueOf(da)-1));
            }
        }
    }

    public void createSortedVertexBuffer(ArrayList<Short> ind,ArrayList<Float> vert,ArrayList<Float> textu,ArrayList<Float> norma) {
        int i;
        for(i=0;i<ind.size();i++){
            if(i%3==0){
                int st = ind.get(i)*3;
                buffer.add(vert.get(st));
                buffer.add(vert.get(st+1));
                buffer.add(vert.get(st+2));
            }else if(i%3==1){
                int st = ind.get(i)*2;
                buffer.add(textu.get(st));
                buffer.add(textu.get(st+1));
            }else if(i%3==2){
                int st = ind.get(i)*3;
                buffer.add(norma.get(st));
                buffer.add(norma.get(st+1));
                buffer.add(norma.get(st+2));
            }
        }
    }

    public void load(String filename) throws IOException{
        buffer.clear();
        ArrayList <Float> vert_coords = new ArrayList <Float> ();
        ArrayList <Float> tex_coords = new ArrayList <Float> ();
        ArrayList <Float> norm_coords = new ArrayList <Float> ();
        ArrayList <Short> all_indices = new ArrayList <Short> ();
        ArrayList <Short> indices = new ArrayList <Short> ();
        InputStream inp = asset.open(filename);
        fileReader = new BufferedReader(new InputStreamReader(inp));
        //Read from the file
        String line = "";
        line = fileReader.readLine();
        boolean k;
        int c=0;
        while(line != null)
        {
            c++;
            System.out.println(c);
            k=false;
            if (line!=""){
                k=true;

            }
            if (k && line.startsWith(VERTEX))
            {
                //Store the vertex in the proper array and continue.
                String[] arr = line.split(" ");
                searchData(arr,vert_coords,"v","float",all_indices);
                line = fileReader.readLine();
            }
            else if (k && line.startsWith(NORMAL))
            {
                String[] arr = line.split(" ");
                searchData(arr,norm_coords,"vn","float",all_indices);
                line = fileReader.readLine();
            }
            else if (k && line.startsWith(FACE))
            {
                //Store the face in the proper array and continue
                String[] arr = line.split(" ");
                int j;
                for(j=1;j<arr.length;j++){
                    String[] va = arr[j].split("/");
                    searchData(va,vert_coords,"f","short",all_indices);
                    indices.add((short) (Integer.valueOf(va[0])-1));
                }

                line = fileReader.readLine();
            }
            else if (k && line.startsWith(TEXTURE))
            {
                String[] arr = line.split(" ");
                searchData(arr,tex_coords,"vt","float",all_indices);
                line = fileReader.readLine();
            }
            else
            {
                line = fileReader.readLine();
            }
        }
        try {
            fileReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        createSortedVertexBuffer(all_indices,vert_coords,tex_coords,norm_coords);
        indi = (ArrayList) indices.clone();
    }


    public ArrayList<Float> getBuffer(){
        return this.buffer;
    }
    public ArrayList<Short> getIndices(){
        return this.indi;
    }
    public int getBufferSize(){
        return this.buffer.size();
    }
    public int getIndicesSize(){
        return this.indi.size();
    }

}
