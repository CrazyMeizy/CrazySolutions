public class SumDoubleSpace {
    public static void main(String[] args){
        double sum=0;
        for (String s:args) {
            StringBuilder sumi= new StringBuilder();
            for ( int i =0;i<s.length();i++){
                if (Character.getType(s.charAt(i))!=Character.SPACE_SEPARATOR){
                    sumi.append(s.charAt(i));
                } else {
		    if(sumi.length()>0){
                        sum+=Double.parseDouble(sumi.toString());
                    }
                    sumi.setLength(0);
                }
            }
            if(sumi.length()>0){
                sum+=Double.parseDouble(sumi.toString());
            }
        }
        System.out.println(sum);
    }
}
