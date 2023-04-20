package COPProject;
/*
 * Date:4/14/2023
 * Name Steven Luciano-Aguilar
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

abstract class Lecture{
    private String crn;
    private String prefix;
    private String titl;
    private String grad;
    private String building;
    private String labs;
    private String modality;
    private Student[] list;

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getCRN() {
        return crn;
    }
    public void setCRN(String cRN) {
        crn = cRN;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getTitl() {
        return titl;
    }
    public void setTitl(String titl) {
        this.titl = titl;
    }
    public String getGrad() {
        return grad;
    }
    public void setGrad(String grad) {
        this.grad = grad;
    }
    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public String getLabs() {
        return labs;
    }
    public void setLabs(String labs) {
        this.labs = labs;
    }
    
} 

class LectureLabsNoLabs extends Lecture{
    
    public LectureLabsNoLabs(String crn, String prefix, String titl, String grad, String modality, String building, String labs) {
        setCRN(crn);
        setPrefix(prefix);
        setTitl(titl);
        setGrad(grad);
        setModality(modality); 
        setBuilding(building);
        setLabs(labs);
    }

    @Override
    public String toString() {
        return getCRN() + "," + getPrefix() + "," + getTitl() + "," + getGrad() + "," + getModality() + "," + getBuilding() + "," + getLabs();
    }
    
}

class LabClass extends Lecture{
    public LabClass(String crn, String building){
        setCRN(crn);
        setBuilding(building);
    }

    @Override
    public String toString(){
        return getCRN() + "," + getBuilding();
    }
} 

class Online extends Lecture{
    public Online(String crn, String prefix, String titl, String grad, String modality) {
        setCRN(crn);
        setPrefix(prefix);
        setTitl(titl);
        setGrad(grad);
        setModality(modality); 
    }
    @Override
    public String toString() {
        return getCRN() + "," + getPrefix() + "," + getTitl() + "," + getGrad() + "," + getModality();
    }
}

class LectureList{
    private Lecture[] list;
    private int size;
    private Scanner myScan;
    public Lecture[] getList() {
        return list;
    }

    public void setList(Lecture[] list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void listLabs(String crn, String building, int counter){
        list[counter] = new LabClass(crn, building);
    }
    public void listLecture(String crn, String prefix, String titl, String grad, String modality, String building, String labs, int counter){
        list [counter] = new LectureLabsNoLabs(crn, prefix, titl, grad, modality, building, labs);
    }
    public void listOnline(String crn, String prefix, String titl, String grad, String modality, int counter){
        list [counter] = new Online(crn, prefix, titl, grad, modality);
    }

    public LectureList(String FILE, Scanner myScan){
        boolean done = false;
        this.myScan = myScan;
        
        while(!done){
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
                System.out.print("File Found! Let's proceed...\n");
                int lines = 0;
                while (reader.readLine() != null) lines++;
                size = lines;
                reader.close();
                done = true;
            } catch (IOException e) {
                System.out.print("Sorry no such file.\n");
                System.out.print("Try again:");
                FILE = myScan.nextLine();
            }
        }
        //myScan.close();
        list = new Lecture[size];
        for(int i = 0; i < size; i++) list[i]= null;

        FillList(FILE);
        CleanFile(FILE);
    }

    private void FillList(String FILE){
        int counter = 0;
        try (Scanner reader = new Scanner(new File(FILE))) {
            while (reader.hasNextLine()){
                String input = reader.nextLine();
                String[] parts = input.split(",");
                int check = parts.length;

                if(check == 7){
                    listLecture(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], counter);
                }else if(check == 5){
                    listOnline(parts[0], parts[1], parts[2], parts[3], parts[4], counter);
                }else if(check == 2){
                    listLabs(parts[0], parts[1], counter);
                }
                counter++;
            }           
            reader.close();//close the file
        } catch (Exception e) {
            System.out.print("Somthing went wrong\n");
        }
        
    }

    public void classLookUp(String CRN){
        String [] parts = CRN.split(" ");
        int inputs = parts.length;
        for(int j = 0; j < inputs; j++){
            for(int i = 0; i < size; i ++){
                if(list[i].getCRN() != null){
                    if(list[i].getCRN().compareTo(parts[j]) == 0){
                        if(list[i] instanceof LectureLabsNoLabs){
                            if(list[i].getLabs().compareTo("No") == 0){
                                System.out.print("\t ["+list[i].getCRN()+ "/" + list[i].getPrefix()+"/"+ list[i].getTitl()+"] Added!\n");
                            }else{
                                System.out.print("\t ["+ list[i].getPrefix()+"/"+ list[i].getTitl()+"] has these labs:\n");
                                printLab(i);
                            }
                        }else{
                            System.out.print("\t ["+list[i].getCRN()+ "/" + list[i].getPrefix()+"/"+ list[i].getTitl() +"] Added!\n"); 
                        }
                    }
                }
            }
        }
    }

    public void printLab(int index){
        index = index + 1;
        int end = index +3;

        for(int i = index; i < end; i++){
            System.out.print("\t \t "+list[i]+"\n");
        }
    }

    public void deleteLecture(String CRN){
        for(int i = 0; i < size; i ++){
            if(list[i].getCRN().compareTo(CRN) == 0){
                if(list[i] instanceof LectureLabsNoLabs){
                    if(list[i].getLabs().compareTo("No") == 0){
                        list[i] = null;
                    }else{
                        list[i] = null;
                        deleteLab(i);
                    }
                }else{
                    list[i] = null; 
                }
            }
        }
    }

    public void deleteLab(int index){
        index = index + 1;
        int end = index +3;

        for(int i = index; i < end; i++){
            list[i] = null;
        }

    }
    

    public void rewrite(){
        try {
            FileWriter myWriter = new FileWriter("lec.txt");
            for (Lecture l:list){
                myWriter.write(l +"\n");
            }
            myWriter.close();
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    private void CleanFile(String FILE){
        try {
            new FileWriter(FILE, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Student{
    private int id;
    private String name;
    private Lecture [] link;

     
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Lecture[] getLink() {
        return link;
    }
    public void setLink(Lecture[] link) {
        this.link = link;
    }
}

class TA extends Student{
    private String supervisor;
    private String degree;

    public String getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    
}

class Faculty extends Student{
    private String rank;
    private String office;
    
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    
}

class UserList{
    private Student[] list;
    private int size = 0;

    public void expand(){
        Student[] newList = new Student[list.length + 1];
        System.arraycopy(list, 0, newList, 0, list.length);

        list = newList;
    }

    public void newUser(){
        size++;
        expand();

    }

    public void listStudent(){

    }
    

}







public class FinalProject {
    public static void main(String[] args) {
        boolean keepGoing = true;
        int choice;
        Scanner myScan = new Scanner(System.in);

        System.out.print("Enter the absolute path of the file:");
        String FILE = myScan.next();
        myScan.nextLine();//flush

        LectureList list = new LectureList(FILE,myScan);
        
        do{
            
            System.out.print("***********************************************\n");
            System.out.print("Choose one of these options:\n");
            System.out.print("\t 1- Add a new Faculty to the schedule\n"); 
            System.out.print("\t 2- Enroll a Student to a Lecture\n");
            System.out.print("\t 3- Print the schedule of a Faculty\n"); 
            System.out.print("\t 4- Print the schedule of an TA\n");
            System.out.print("\t 5- Print the schedule of a Student\n"); 
            System.out.print("\t 6- Delete a Lecture\n"); 
            System.out.print("\t 7- Exit\n");
            System.out.print("Enter your choice:"); 
            

            choice = myScan.nextInt();
            myScan.nextLine();

            switch(choice){
                case 1:
                    //get faculty ID
                    //get faculty name 
                    //get faculty office
                    //get number lecturs
                    
                    System.out.print("Enter the crns of the lectures:"); //get CRNS
                    list.classLookUp(myScan.nextLine());//Do a look up through CRN
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.print("Enter the CRN of the lecture to delete:");
                    list.deleteLecture(myScan.nextLine()); //get CRN (Not Woorking "Cannot invoke "Lecture.getCRN()" because "this.list[<local2>]" is null")
                    break;
                case 7:
                    keepGoing = false;
                    break;
                default:
                    System.out.print("Pleae choose the proper choices\n");
            }

        }while(keepGoing);

        list.rewrite();
        
        System.out.print("Done");
        myScan.close();
    }
}
