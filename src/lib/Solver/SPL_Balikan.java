package lib.Solver;

import java.util.Scanner;

import lib.Matrix.Matrix;
import lib.Utils.IO;

public class SPL_Balikan {

    // Menggunakan GaussJordan
    public static Matrix INV_GaussJordan(Matrix inverse){
        /*I.S. Matrix inverse terdefinisi dan isSquare()*/
        /*F.S. Matrix inverse sudah berubah menjadi inverse */
        /*F.S. Jika matrix tidak memiliki balikan, akan diberikan matrix kosong*/

        // Kamus Lokal
        int i,j;

        // Membuat matrix augmented dari inverse
        Matrix inverse2 = new Matrix();
        Matrix inverse3 = new Matrix();

        Matrix.copyMatrix(inverse, inverse2);

        inverse3.row = inverse.row;
        inverse3.col = inverse.col;
        inverse2.col*=2;

        if(Determinan.DET_Gauss(inverse)!=0.0){
            for(i=0;i<inverse.row;i++){
                for(j=inverse.col;j<inverse2.col;j++){
                    if(i==(j-inverse.col)){
                        inverse2.array[i][j]=1.0;
                    } else{
                        inverse2.array[i][j]=0.0;
                    }
                }
            }

            // Melakukan GaussJordan sehingga matrix indentitas berpindah ke kiri
            inverse2.Hasil_OBE();


            // Mengcopy kembali hasil balikan ke Matrix inverse3
            for(i=0;i<inverse.row;i++){
                for(j=0;j<inverse.col;j++){
                    inverse3.array[i][j]=inverse2.array[i][j+inverse.col];
                }
            }
        }
        inverse3.mintoZero();
        return inverse3;
    }

    // Menggunakan Ax=b
    public static Matrix Balikan_SPL(Matrix inverse){
        /*I.S. Matrix inverse terdefinisi dan isSquare()*/
        /*F.s. Matrix inverse sudah berubah menjadi inverse */
        /*F.S. Jika matrix tidak memiliki balikan, akan diberikan matrix kosong*/

        // KAMUS LOKAL
        double det; 
        int i,j;
        int x,y;
        int m,n;
        int tanda;
        Matrix esrevni = new Matrix();

        // ALGORITMA
        det = Determinan.DET_Gauss(inverse);
        esrevni.row = inverse.row;
        esrevni.col = inverse.col;

        if(det!=0.0){
            Matrix.copyMatrix(inverse, esrevni);
            if (inverse.row==2){
                esrevni.array[0][0]=inverse.array[1][1];
                esrevni.array[0][1]=(-1)*inverse.array[1][0];
                esrevni.array[1][0]=(-1)*inverse.array[0][1];
                esrevni.array[1][1]=inverse.array[0][0];
                for(i=0;i<esrevni.row;i++){
                    esrevni.KaliMatriks(1/det, i);
                }
            } else{
                
                for(i=0;i<inverse.row;i++){
                    if(i%2==0){
                        tanda = 1;
                    } else{
                        tanda = -1;
                    }
                    for(j=0;j<inverse.row;j++){
                        
                        // Mengisi matrix kofaktor ke dalam adjoint
                        Matrix MatKof = new Matrix();
                        MatKof.row=inverse.row-1;
                        MatKof.col=inverse.row-1;

                        m = 0;
                        n = 0;
                        for(x=0;x<inverse.row;x++){
                            for(y=0;y<inverse.col;y++){
                                if(x!=i && y!=j){
                                    
                                    MatKof.array[m][n]=inverse.array[x][y];
                                    if(n==MatKof.col-1){
                                        n=0;
                                        m++;
                                    }else{
                                        n++;
                                    }
                                }
                            }
                        }
                        esrevni.array[i][j]=tanda*(Determinan.DET_Gauss(MatKof));
                        tanda*=-1;
                    }
                }
                Matrix.transpose(esrevni);
                for(i=0;i<esrevni.row;i++){
                    esrevni.KaliMatriks(1/det, i);
                }
                
            }
        }
        esrevni.mintoZero();
        return esrevni;
    }

    public static String[] Hasil_INV(Matrix Axb){
        /* I.S. Matrix NxN+1 terdefinisi dengan bentuk Ax=b, baris = N , kolom = N+1 */
        /* F.S  Solusi dari x dengan metode x = A^-1 * b*/
        
        // KAMUS LOKAL
        int i,j;
        Matrix Ax = new Matrix();
        Ax.row = Axb.row;
        Ax.col = Axb.row;
        Matrix invAx = new Matrix();
        Matrix b = new Matrix();
        b.row = Axb.row;
        b.col = 1;
        Matrix hasil = new Matrix();
        hasil.row = Axb.row;
        hasil.col = 1;

        // ALGORITMA
        
        // isi Matrix Ax ukuran NxN
        for(i=0;i<Ax.row;i++){
            for(j=0;j<Ax.col;j++){
                Ax.array[i][j]=Axb.array[i][j];
            }
        }
        
        // isi Matrix b ukuran Nx1
        for(i=0;i<Ax.row;i++){
            b.array[i][0]=Axb.array[i][Axb.col-1];
        }
        
        // balikan dari Ax
        invAx = INV_GaussJordan(Ax);
        
        String solution[];

        if(!invAx.isEmpty()){
            // hasil perkalian invAx dan b
            hasil = Matrix.KaliMatrix(invAx, b);
            System.out.println("Solusi dari persamaan diatas adalah");
            solution = new String[hasil.row];
            for(i=0;i<hasil.row;i++){
                solution[i] = "x"+(i+1)+" = "+hasil.array[i][0];
                System.out.printf("X%d = %.2f\n", i+1,hasil.array[i][0]);
                //System.out.println("X"+(i+1)+": "+hasil.array[i][0]);
            }
        } else{
            solution = new String[1];
            solution[0] = "Tidak memiliki solusi";
            System.out.println("Solusi tidak ada");
        }
        return solution;
    }
    public static void main() {
        Scanner input = new Scanner(System.in);
        Scanner input3 = new Scanner(System.in);
        Scanner input2 = new Scanner(System.in);
        String file ;
        String[] solusi;
        Matrix solver = new Matrix();
        do{
            System.out.print("Input file (y/n) : ");
            file = input.nextLine();
        }while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            solver = IO.inputMatrixFile(filename);
            }
        else{
            System.out.print("Masukkan jumlah m: ");
            int m = input3.nextInt();
            System.out.print("Masukkan jumlah n: ");
            int n = input2.nextInt();
            solver.IsiMatriks(m, n);
            }
            solusi = Hasil_INV(solver);
            
        do{
            System.out.print("Simpan solusi ke file (y/n) : ");
            file = input.nextLine();
        }
        while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            IO.outputOBEFile(filename, solusi);
        }
    }  
    public static void mainInvGauss() {
        Scanner input = new Scanner(System.in);
        Scanner input3 = new Scanner(System.in);
        Scanner input2 = new Scanner(System.in);
        String file ;
        Matrix solver = new Matrix();
        do{
            System.out.print("Input file (y/n) : ");
            file = input.nextLine();
        }while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            solver = IO.inputMatrixFile(filename);
            }
        else{
            System.out.print("Masukkan jumlah n: ");
            int n = input2.nextInt();
            solver.IsiMatriks(n, n);
            }
            Matrix inv = new Matrix();
            inv = INV_GaussJordan(solver);
            System.out.println("Hasil invers Gauss Jordan");
            inv.Display();
        do{
            System.out.print("Simpan solusi ke file (y/n) : ");
            file = input.nextLine();
        }
        while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            IO.outputInversFile(filename, inv);
        }
    }   
    public static void mainInvAdj() {
        Scanner input = new Scanner(System.in);
        Scanner input3 = new Scanner(System.in);
        Scanner input2 = new Scanner(System.in);
        String file ;
        Matrix solver = new Matrix();
        do{
            System.out.print("Input file (y/n) : ");
            file = input.nextLine();
        }while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            solver = IO.inputMatrixFile(filename);
            }
        else{
            System.out.print("Masukkan jumlah n: ");
            int n = input2.nextInt();
            solver.IsiMatriks(n, n);
            }
            Matrix inv = new Matrix();
            inv = Balikan_SPL(solver);
            System.out.println("Hasil invers Adjoin");
            inv.Display();
        do{
            System.out.print("Simpan solusi ke file (y/n) : ");
            file = input.nextLine();
        }
        while(!file.equals("y") && !file.equals("n") && !file.equals("Y") && !file.equals("N"));
        if(file.equals("y") || file.equals("Y")){
            System.out.print("Masukkan nama file (filename.txt): ");
            String filename = input.nextLine();
            IO.outputInversFile(filename, inv);
        }
    }
    // public static void main(String[] args){
    //     Matrix a = new Matrix();
    //     Matrix b = new Matrix();
    //     Matrix c = new Matrix();
    //     int ukuran = 16;
    //     a.IsiMatriks(ukuran, ukuran);
    //     b = INV_GaussJordan(a);
    //     c = Balikan_SPL(a);
    //     System.out.println("Hasil sebelum inverse adalah :");
    //     a.Display();
    //     System.out.println("Hasil inverse adalah :");
    //     b.Display();
    //     System.out.println("Hasil inverse adalah :");
    //     c.Display();
    // }
}
