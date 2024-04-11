public class Sum{
    public static void main(String[] args){
        int i = 0;
        int sum = 0;
        while(args.length>i){
            String s = args[i++];
            int sumi=0;
            int flag=0;
            for (int j =0;j<s.length();j++){
                if ((s.charAt(j)>='0') & (s.charAt(j)<='9')){
                sumi*=10;
                sumi+=Integer.parseInt(""+s.charAt(j));
                } else{
                    if (flag==0){
                        sum+=sumi;
                    } else {
                        sum-=sumi;
                    }
                    sumi=0;
                    flag=0;
                    if (s.charAt(j)=='-'){
                        flag=1;
                    }
                }
            }
            if (flag==0){
                sum+=sumi;
            } else {
                sum-=sumi;
            }
        }
        System.out.println(sum); 
    }
}