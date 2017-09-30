package com.github.broncho.sigar;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Author: ZhangXiao
 * Created: 2017/9/30
 */
public class GoSigarTest {
    
    
    public static void main(String[] args) {
    
        Sigar sigar = GoSigar.sigar();
        try {
            System.out.println(sigar.getCpu());
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }
}
