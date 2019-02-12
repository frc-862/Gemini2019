/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.paths;

public class ArcSimple extends Path {
    @Override
    public double[][] getLeftPath() {
        return this.Left;
    }

    @Override
    public double[][] getRightPath() {
        return this.Right;
    }

    public double[][] Left = new double[][] {
        {0,0,0,-0.0003},
        {0.0021,0.1027,5.0083,-0.0068},
        {0.0077,0.2831,9.1838,-0.0296},
        {0.0169,0.4627,8.9751,-0.0741},
        {0.0297,0.6425,9.0198,-0.1413},
        {0.0462,0.8226,9.0022,-0.2311},
        {0.0661,1.0018,8.9965,-0.3439},
        {0.0898,1.1814,8.9758,-0.4802},
        {0.117,1.36,8.9424,-0.6406},
        {0.1478,1.5391,8.9295,-0.8258},
        {0.1821,1.7164,8.8802,-1.0364},
        {0.2199,1.8937,8.8768,-1.2726},
        {0.2613,2.0701,8.8167,-1.5362},
        {0.3063,2.2455,8.7635,-1.8281},
        {0.3547,2.42,8.7224,-2.1494},
        {0.4065,2.593,8.6555,-2.5013},
        {0.4619,2.7653,8.6094,-2.8853},
        {0.5206,2.9356,8.5137,-3.3032},
        {0.5826,3.1044,8.4472,-3.7564},
        {0.648,3.2717,8.3666,-4.2473},
        {0.7168,3.4369,8.2531,-4.7785},
        {0.7887,3.5994,8.1344,-5.3517},
        {0.8639,3.7604,8.0528,-5.97},
        {0.9424,3.9186,7.9011,-6.637},
        {1.0238,4.0728,7.7159,-7.3552},
        {1.1083,4.2254,7.6293,-8.1282},
        {1.1958,4.3738,7.418,-8.961},
        {1.2861,4.5177,7.1962,-9.857},
        {1.3793,4.6587,7.0509,-10.8211},
        {1.4752,4.7945,6.7867,-11.8591},
        {1.5737,4.9253,6.5408,-12.9763},
        {1.6747,5.0508,6.277,-14.1783},
        {1.7782,5.1715,6.032,-15.472},
        {1.8838,5.2851,5.68,-16.864},
        {1.991,5.358,3.6487,-18.3564},
        {2.0954,5.2184,-6.9826,-19.9227},
        {2.1969,5.0751,-7.1676,-21.5311},
        {2.2957,4.9439,-6.5613,-23.1765},
        {2.3922,4.8225,-6.0673,-24.8582},
        {2.4864,4.7088,-5.6849,-26.5747},
        {2.5784,4.6049,-5.1947,-28.3233},
        {2.6687,4.5117,-4.6619,-30.1024},
        {2.7572,4.4273,-4.2176,-31.9104},
        {2.8443,4.3531,-3.7116,-33.744},
        {2.9301,4.2898,-3.1661,-35.6004},
        {3.0149,4.2371,-2.6341,-37.4768},
        {3.0987,4.1942,-2.1446,-39.3692},
        {3.182,4.1634,-1.5414,-41.2735},
        {3.2648,4.1432,-1.0087,-43.1858},
        {3.3476,4.1353,-0.3927,-45.1027},
        {3.4303,4.1378,0.1228,-47.0209},
        {3.5133,4.1511,0.6665,-48.9349},
        {3.5969,4.1773,1.3099,-50.841},
        {3.6811,4.2138,1.8232,-52.7356},
        {3.7664,4.2619,2.4066,-54.6149},
        {3.8528,4.3211,2.9573,-56.4758},
        {3.9406,4.3902,3.4547,-58.3143},
        {4.0301,4.4713,4.0583,-60.127},
        {4.1213,4.562,4.5355,-61.9116},
        {4.2146,4.6639,5.0912,-63.6658},
        {4.3101,4.7745,5.5303,-65.3871},
        {4.408,4.8962,6.0855,-67.0733},
        {4.5085,5.0271,6.5454,-68.7235},
        {4.6119,5.1675,7.019,-70.3364},
        {4.7173,5.2722,5.2322,-71.905},
        {4.8207,5.1701,-5.1068,-73.3963},
        {4.922,5.0606,-5.4688,-74.7852},
        {5.0208,4.9463,-5.7208,-76.0748},
        {5.1174,4.8277,-5.9264,-77.2715},
        {5.2115,4.7028,-6.244,-78.3824},
        {5.3029,4.5723,-6.5285,-79.4132},
        {5.3917,4.4377,-6.7253,-80.3698},
        {5.4776,4.2973,-7.022,-81.2575},
        {5.5607,4.1536,-7.1869,-82.0808},
        {5.6408,4.0056,-7.3965,-82.8444},
        {5.7179,3.8542,-7.5747,-83.5524},
        {5.7919,3.6994,-7.7371,-84.2087},
        {5.8627,3.5416,-7.8848,-84.8167},
        {5.9304,3.381,-8.0359,-85.3793},
        {5.9947,3.2181,-8.1448,-85.8992},
        {6.0558,3.0533,-8.2373,-86.3794},
        {6.1135,2.8856,-8.3824,-86.8221},
        {6.1678,2.7167,-8.4477,-87.2292},
        {6.2188,2.5461,-8.5272,-87.6026},
        {6.2662,2.3738,-8.6159,-87.9442},
        {6.3103,2.2004,-8.6672,-88.2555},
        {6.3508,2.0255,-8.7409,-88.5378},
        {6.3878,1.8497,-8.798,-88.7923},
        {6.4212,1.6731,-8.8286,-89.0201},
        {6.4511,1.4955,-8.884,-89.2223},
        {6.4775,1.3172,-8.8997,-89.3997},
        {6.5003,1.1378,-8.9685,-89.5532},
        {6.5195,0.9583,-8.9624,-89.6831},
        {6.535,0.7782,-9.0319,-89.7899},
        {6.547,0.5982,-8.9737,-89.8743},
        {6.5553,0.4167,-9.037,-89.9367},
        {6.5601,0.2347,-9.02,-89.9771}
    };

    public double[][] Right = new double[][] {
        {0,0,0,-0.0003},
        {0.0023,0.1138,5.5473,-0.0068},
        {0.0087,0.3235,10.6795,-0.0296},
        {0.0195,0.5404,10.8407,-0.0741},
        {0.0347,0.76,11.0156,-0.1413},
        {0.0542,0.9794,10.9641,-0.2311},
        {0.0781,1.1994,11.0463,-0.3439},
        {0.1065,1.4191,10.9775,-0.4802},
        {0.1393,1.6404,11.0769,-0.6406},
        {0.1766,1.8616,11.0321,-0.8258},
        {0.2182,2.0844,11.1633,-1.0364},
        {0.2643,2.3065,11.1159,-1.2726},
        {0.315,2.53,11.168,-1.5362},
        {0.3701,2.7548,11.2388,-1.8281},
        {0.4297,2.9804,11.2736,-2.1494},
        {0.4938,3.2074,11.3558,-2.5013},
        {0.5626,3.4352,11.38,-2.8853},
        {0.6359,3.665,11.4928,-3.3032},
        {0.7137,3.896,11.5584,-3.7564},
        {0.7963,4.1285,11.6255,-4.2473},
        {0.8836,4.3635,11.743,-4.7785},
        {0.9755,4.6008,11.8786,-5.3517},
        {1.0723,4.8396,11.9394,-5.97},
        {1.174,5.0816,12.0869,-6.637},
        {1.2805,5.3274,12.3015,-7.3552},
        {1.392,5.5746,12.363,-8.1282},
        {1.5086,5.8263,12.5741,-8.961},
        {1.6302,6.0824,12.8148,-9.857},
        {1.757,6.3413,12.9425,-10.8211},
        {1.8891,6.6055,13.2094,-11.8591},
        {2.0266,6.8747,13.4585,-12.9763},
        {2.1696,7.1492,13.7257,-14.1783},
        {2.3182,7.4285,13.96,-15.472},
        {2.4725,7.7149,14.3236,-16.864},
        {2.6317,7.962,12.3536,-18.3564},
        {2.7908,7.952,-0.4977,-19.9227},
        {2.9484,7.8822,-3.4895,-21.5311},
        {3.1047,7.8161,-3.306,-23.1765},
        {3.2598,7.756,-3.0063,-24.8582},
        {3.4139,7.7039,-2.6056,-26.5747},
        {3.567,7.6577,-2.3116,-28.3233},
        {3.7193,7.6165,-2.0572,-30.1024},
        {3.871,7.5821,-1.7201,-31.9104},
        {4.022,7.5532,-1.4455,-33.744},
        {4.1726,7.529,-1.2095,-35.6004},
        {4.3229,7.5098,-0.9606,-37.4768},
        {4.4728,7.4964,-0.6713,-39.3692},
        {4.6226,7.4865,-0.4938,-41.2735},
        {4.7721,7.4816,-0.2462,-43.1858},
        {4.9217,7.48,-0.0808,-45.1027},
        {5.0715,7.4837,0.1857,-47.0209},
        {5.2213,7.4922,0.426,-48.9349},
        {5.3713,7.5035,0.567,-50.841},
        {5.5217,7.5204,0.8411,-52.7356},
        {5.6725,7.5413,1.0469,-54.6149},
        {5.8239,7.5671,1.2883,-56.4758},
        {5.9759,7.5989,1.5892,-58.3143},
        {6.1286,7.6345,1.7836,-60.127},
        {6.2821,7.6768,2.112,-61.9116},
        {6.4366,7.724,2.3633,-63.6658},
        {6.5922,7.7789,2.744,-65.3871},
        {6.7489,7.8391,3.0082,-67.0733},
        {6.9071,7.9066,3.3777,-68.7235},
        {7.0667,7.9815,3.7424,-70.3364},
        {7.2269,8.0092,1.3851,-71.905},
        {7.3823,7.7738,-11.7743,-73.3963},
        {7.5321,7.4833,-14.5197,-74.7852},
        {7.676,7.1977,-14.2857,-76.0748},
        {7.8143,6.9163,-14.0689,-77.2715},
        {7.9471,6.6412,-13.7519,-78.3824},
        {8.0745,6.3718,-13.4753,-79.4132},
        {8.1967,6.1063,-13.2652,-80.3698},
        {8.3137,5.8467,-12.9838,-81.2575},
        {8.4255,5.5905,-12.8124,-82.0808},
        {8.5322,5.3384,-12.603,-82.8444},
        {8.634,5.0899,-12.4246,-83.5524},
        {8.7309,4.8447,-12.2613,-84.2087},
        {8.823,4.6024,-12.112,-84.8167},
        {8.9103,4.363,-11.969,-85.3793},
        {8.9927,4.1259,-11.8582,-85.8992},
        {9.0706,3.8908,-11.7507,-86.3794},
        {9.1438,3.6583,-11.6242,-86.8221},
        {9.2123,3.4272,-11.5547,-87.2292},
        {9.2763,3.1978,-11.4691,-87.6026},
        {9.3357,2.9701,-11.3874,-87.9442},
        {9.3906,2.7434,-11.3273,-88.2555},
        {9.4409,2.5182,-11.2599,-88.5378},
        {9.4868,2.2941,-11.2108,-88.7923},
        {9.5282,2.0707,-11.1675,-89.0201},
        {9.5652,1.8485,-11.1182,-89.2223},
        {9.5977,1.6265,-11.0809,-89.3997},
        {9.6259,1.4055,-11.0455,-89.5532},
        {9.6496,1.1847,-11.0255,-89.6831},
        {9.6688,0.9651,-11.0093,-89.7899},
        {9.6838,0.7451,-10.9737,-89.8743},
        {9.6943,0.5252,-10.9483,-89.9367},
        {9.7005,0.3046,-10.9298,-89.9771}
    };

}
