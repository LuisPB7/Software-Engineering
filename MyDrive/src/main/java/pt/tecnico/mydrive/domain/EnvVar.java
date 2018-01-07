package pt.tecnico.mydrive.domain;

public class EnvVar extends EnvVar_Base {
    
    public EnvVar(String n, String v) {
        setName(n);
        setValue(v);
    }
    
//    @Override
//    public void setVar(Session s){
//        for(EnVar var: s.getVariableListSet()){
//        	if
//        }
//    }
    
    public String list(){
    	return getName() + "=" + getValue();
    }
    
}
