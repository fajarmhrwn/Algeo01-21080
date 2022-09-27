
package lib.Solver;
import java.util.Scanner;
import lib.Matrix.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.Math;

public class interpolationPolinom {
    Matrix point = new Matrix();
    int N;
    double [] konstanta = new double [N];
    String persamaan;
    public void interpolasiInput(){
        /*
         Membuat matriks dari masukan user
         I.S. Masukkan jumlah N (banyaknya masukkan titik)
         F.S. Terbentuk Matriks
         */
        System.out.print("Masukkan Input N: ");
        Scanner myobj = new Scanner(System.in);
        this.N = myobj.nextInt();
        this.point.col = this.N+1;
        this.point.row = this.N;
        for (int i=0;i<N;i++){
            System.out.print("Masukkan x y: ");
            double x = myobj.nextDouble();
            double y = myobj.nextDouble();
            for (int j=0;j<N+1;j++){
                if (j != N){
                    this.point.array[i][j] = Math.pow(x, j);
                }else{
                    this.point.array[i][j] = y;
                }
            }
        }
        this.point.Display();
        myobj.close();
    }
    
    public void persamaanPolinom(){
        /*
         Menyelesaikan matriks Persamaan Polinom dengan menggunakan metode Gauss
         I.S. Sembarang
         F.S. Terbentuk Persamaan Polinom
         */
        double [] solution = new double[this.N];
        this.point.Hasil_OBE();
        // Substitusi Mundur
        for (int i = this.N - 1;i>=0;i--){
            double sum = 0;
            for (int j = i+1;j<=this.N-1;j++){
                sum = sum + solution[j]*this.point.array[i][j];
            }
            solution[i] = (1/this.point.array[i][i])*(this.point.array[i][this.N]-sum);
        }
        this.konstanta = solution;
        this.persamaan = "P(x) = ";
        //Print Persamaan Polinom
        for (int i = 0;i<this.N;i++){
            if (i==0){
            this.persamaan = this.persamaan+Double.toString(solution[i]);
            }
            else{
                if (i != this.N){
                    if (solution[i] != 0 && solution[i]!=1){
                        if (solution[i]>0){
                            this.persamaan = this.persamaan+" + "+Double.toString(solution[i])+" X^"+Integer.toString(i);
                        }else{
                            this.persamaan = this.persamaan+" - "+Double.toString(-1*solution[i])+" X^"+Integer.toString(i);
                        }
                    }else if (solution[i] == 1 && i!=1){
                        this.persamaan = this.persamaan+" + X^"+Integer.toString(i);
                    }else if (solution[i] == 1 && i==1){
                        this.persamaan = this.persamaan+" + X";
                    }
                }
            }
        }
        System.out.println(this.persamaan);
    }
    public String nilaiTaksir(double x){
        /*
         * Spesifikasi Fungsi : diberikan nilai x, menghasilkan nilai taksiran dari x
         */
        double sum = 0;
        for (int i=0;i<this.N;i++){
            sum = sum + this.konstanta[i]*Math.pow(x,i);
        }
        System.out.printf("P(%.2f) = %.2f\n",x,sum);
        return String.format("P(%.2f) = %.2f\n",x,sum);
    }
    public void interpolasiInputFile(String filename){
        /*
         * Membuat matriks dari masukan file
         * I.S. Masukkan nama file
         * F.S. Terbentuk Matriks
         */
            Matrix temp = new Matrix();
        try{
            FileReader file = new FileReader("test/"+filename);
            BufferedReader br = new BufferedReader(file);   
            String line = br.readLine();
            int i = 0;
            while (line != null){
                String [] data = line.split(" ");
                for (int j=0;j<data.length;j++){
                    temp.array[i][j] = Double.parseDouble(data[j]);
                }
                line = br.readLine();
                i = i + 1;
            }
            br.close();
            this.N = i;
            this.point.col = this.N+1;
            this.point.row = this.N;
            for (int j= 0;j<i;j++){
                for (int k=0;k<i+1;k++){
                    if (k != i){
                        this.point.array[j][k] = Math.pow(temp.array[j][0], k);
                    }else{
                        this.point.array[j][k] = temp.array[j][1];
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("File tidak ditemukan");
            System.out.println(e);
        }
    }
    public void interpolasioutputFile(String filename, double x){
        /*
         * Menulis hasil interpolasi ke file
         * I.S. Sembarang
         * F.S. Terbentuk file interpolasi.txt
         */
        try{
            FileWriter file = new FileWriter("hasil/"+filename);
            file.write(this.persamaan);
            file.write("\n");
            file.write(nilaiTaksir(x));
            file.close();
        }
        catch(Exception e){
            System.out.println("File tidak ditemukan");
            System.out.println(e);
        }

    }
    public static void main(String[] args) {
        interpolationPolinom test = new interpolationPolinom();
        test.interpolasiInputFile("1.txt");
        test.persamaanPolinom();
        test.interpolasioutputFile("interpolasi.txt", 5);
    }
}




	