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

    public static double[][] Left = new double[][] {
        {0,0,0,-0.0001},
        {0.002,0.0974,4.8456,-0.0029},
        {0.0076,0.2802,9.1002,-0.0132},
        {0.0169,0.469,9.4829,-0.0335},
        {0.0301,0.6574,9.4285,-0.064},
        {0.047,0.8466,9.457,-0.1051},
        {0.0677,1.0361,9.4759,-0.1571},
        {0.0922,1.2258,9.4965,-0.2203},
        {0.1205,1.4158,9.5201,-0.2951},
        {0.1526,1.6064,9.5105,-0.3825},
        {0.1886,1.7981,9.5961,-0.4829},
        {0.2283,1.9899,9.6025,-0.597},
        {0.272,2.1825,9.6196,-0.7259},
        {0.3195,2.3766,9.7142,-0.8706},
        {0.371,2.571,9.7097,-1.0323},
        {0.4263,2.7674,9.8213,-1.2122},
        {0.4856,2.9643,9.846,-1.4117},
        {0.5488,3.1629,9.9357,-1.6324},
        {0.6161,3.3628,9.9917,-1.8763},
        {0.6874,3.5651,10.1144,-2.1454},
        {0.7628,3.7688,10.1824,-2.4418},
        {0.8423,3.975,10.3135,-2.768},
        {0.9259,4.1834,10.4272,-3.1268},
        {1.0138,4.3942,10.5352,-3.5215},
        {1.1059,4.6091,10.7494,-3.9554},
        {1.2025,4.8261,10.8483,-4.4324},
        {1.3034,5.048,11.0978,-4.9568},
        {1.4089,5.273,11.2451,-5.5336},
        {1.519,5.5039,11.5464,-6.1682},
        {1.6337,5.7393,11.7745,-6.866},
        {1.7533,5.9803,12.0486,-7.6342},
        {1.8779,6.2283,12.4028,-8.4803},
        {2.0076,6.4834,12.7514,-9.4126},
        {2.1426,6.7465,13.1508,-10.4402},
        {2.2829,7.0186,13.6094,-11.573},
        {2.4289,7.2997,14.0523,-12.8219},
        {2.5807,7.5917,14.6031,-14.1988},
        {2.7377,7.8463,12.7291,-15.7113},
        {2.8939,7.8108,-1.7752,-17.3309},
        {3.0481,7.7105,-5.017,-19.0198},
        {3.2005,7.6198,-4.532,-20.7712},
        {3.3513,7.5437,-3.808,-22.583},
        {3.501,7.4789,-3.2385,-24.4528},
        {3.6495,7.4266,-2.6158,-26.3771},
        {3.7971,7.3827,-2.1958,-28.352},
        {3.9441,7.3491,-1.681,-30.3731},
        {4.0906,7.3222,-1.3443,-32.4348},
        {4.2366,7.3033,-0.9459,-34.5309},
        {4.3824,7.2888,-0.7254,-36.6558},
        {4.5281,7.282,-0.3388,-38.8025},
        {4.6736,7.279,-0.1474,-40.9626},
        {4.8192,7.2807,0.0839,-43.1288},
        {4.9649,7.2874,0.3373,-45.2934},
        {5.1109,7.2975,0.5011,-47.4491},
        {5.2572,7.3133,0.7931,-49.5892},
        {5.4039,7.3336,1.0112,-51.7066},
        {5.551,7.3604,1.3403,-53.7941},
        {5.6989,7.3907,1.5191,-55.8464},
        {5.8475,7.4294,1.9346,-57.8595},
        {5.997,7.4759,2.3234,-59.8274},
        {6.1475,7.528,2.6063,-61.7469},
        {6.2994,7.5905,3.1205,-63.6159},
        {6.4526,7.6621,3.5848,-65.4314},
        {6.6074,7.7432,4.0561,-67.1914},
        {6.7641,7.8344,4.5605,-68.8957},
        {6.9229,7.9379,5.1697,-70.5439},
        {7.0838,8.0444,5.3261,-72.1347},
        {7.2418,7.9019,-7.1231,-73.6416},
        {7.3943,7.6207,-14.0549,-75.0332},
        {7.541,7.3397,-14.0589,-76.308},
        {7.6823,7.0669,-13.6395,-77.4768},
        {7.8184,6.8034,-13.1696,-78.5502},
        {7.9494,6.5477,-12.7821,-79.537},
        {8.0754,6.2994,-12.4209,-80.4448},
        {8.1965,6.0565,-12.1386,-81.2812},
        {8.313,5.8199,-11.8281,-82.0528},
        {8.4247,5.5886,-11.5724,-82.7647},
        {8.5319,5.3607,-11.3906,-83.4224},
        {8.6347,5.1377,-11.1516,-84.0307},
        {8.7331,4.9178,-10.9915,-84.5933},
        {8.827,4.7014,-10.8251,-85.1139},
        {8.9169,4.4871,-10.7054,-85.5959},
        {9.0023,4.2765,-10.5406,-86.0419},
        {9.0837,4.0667,-10.4809,-86.4546},
        {9.1609,3.8602,-10.3285,-86.8365},
        {9.234,3.6548,-10.2743,-87.1892},
        {9.303,3.4512,-10.1811,-87.5148},
        {9.368,3.2489,-10.109,-87.8152},
        {9.429,3.0478,-10.0431,-88.0917},
        {9.486,2.8481,-9.9811,-88.3457},
        {9.5389,2.6497,-9.9398,-88.5778},
        {9.588,2.4514,-9.9032,-88.7896},
        {9.6331,2.2547,-9.8318,-88.9822},
        {9.6742,2.0587,-9.8172,-89.156},
        {9.7115,1.8628,-9.7859,-89.3119},
        {9.7449,1.6675,-9.7374,-89.4509},
        {9.7743,1.4732,-9.7285,-89.5731},
        {9.8,1.2785,-9.7069,-89.6791},
        {9.8217,1.0849,-9.684,-89.7695},
        {9.8395,0.8912,-9.6709,-89.8443},
        {9.8535,0.6976,-9.6484,-89.9043},
        {9.8636,0.504,-9.6398,-89.9493},
        {9.8699,0.3102,-9.6318,-89.9796},
        {9.8722,0.1094,-9.345,-89.9953}
    };

    public static double[][] Right = new double[][] {
        {0,0,0,-0.0001},
        {0.0019,0.0925,4.6044,-0.0029},
        {0.0071,0.2623,8.4516,-0.0132},
        {0.0158,0.4335,8.5966,-0.0335},
        {0.0278,0.6041,8.5394,-0.064},
        {0.0433,0.7748,8.5355,-0.1051},
        {0.0622,0.9453,8.526,-0.1571},
        {0.0845,1.1154,8.5106,-0.2203},
        {0.1102,1.2848,8.4905,-0.2951},
        {0.1393,1.4542,8.4531,-0.3825},
        {0.1717,1.6227,8.4301,-0.4829},
        {0.2075,1.7904,8.403,-0.597},
        {0.2467,1.9578,8.3544,-0.7259},
        {0.2891,2.1237,8.3074,-0.8706},
        {0.335,2.2893,8.2671,-1.0323},
        {0.384,2.4532,8.1974,-1.2122},
        {0.4363,2.6161,8.1494,-1.4117},
        {0.4918,2.7774,8.0686,-1.6324},
        {0.5506,2.9374,7.996,-1.8763},
        {0.6125,3.0953,7.8952,-2.1454},
        {0.6776,3.2516,7.8129,-2.4418},
        {0.7456,3.4054,7.6915,-2.768},
        {0.8167,3.5568,7.5765,-3.1268},
        {0.8909,3.7059,7.45,-3.5215},
        {0.9679,3.8511,7.265,-3.9554},
        {1.0478,3.994,7.1409,-4.4324},
        {1.1304,4.1321,6.9108,-4.9568},
        {1.2158,4.2671,6.7427,-5.5336},
        {1.3037,4.3963,6.4607,-6.1682},
        {1.3941,4.5208,6.2286,-6.866},
        {1.4869,4.6397,5.9452,-7.6342},
        {1.5819,4.7516,5.5964,-8.4803},
        {1.679,4.8566,5.2466,-9.4126},
        {1.7781,4.9535,4.8464,-10.4402},
        {1.879,5.0414,4.3928,-11.573},
        {1.9814,5.1202,3.9432,-12.8219},
        {2.0851,5.1882,3.3965,-14.1988},
        {2.1893,5.2074,0.964,-15.7113},
        {2.289,4.9845,-11.1447,-17.3309},
        {2.3842,4.7627,-11.0955,-19.0198},
        {2.4755,4.5639,-9.9365,-20.7712},
        {2.5631,4.3815,-9.1205,-22.583},
        {2.6475,4.217,-8.2229,-24.4528},
        {2.7288,4.0679,-7.4562,-26.3771},
        {2.8076,3.9372,-6.5349,-28.352},
        {2.884,3.8219,-5.7644,-30.3731},
        {2.9585,3.7249,-4.8516,-32.4348},
        {3.0314,3.6442,-4.0382,-34.5309},
        {3.103,3.5827,-3.0709,-36.6558},
        {3.1738,3.5368,-2.2977,-38.8025},
        {3.244,3.5098,-1.3463,-40.9626},
        {3.314,3.5008,-0.4506,-43.1288},
        {3.3842,3.509,0.4107,-45.2934},
        {3.4549,3.5361,1.3517,-47.4491},
        {3.5265,3.5793,2.1591,-49.5892},
        {3.5993,3.64,3.0358,-51.7066},
        {3.6736,3.716,3.8029,-53.7941},
        {3.7498,3.8103,4.7136,-55.8464},
        {3.8282,3.9183,5.3958,-57.8595},
        {3.909,4.0405,6.1122,-59.8274},
        {3.9926,4.1791,6.9295,-61.7469},
        {4.0792,4.3298,7.5322,-63.6159},
        {4.169,4.4936,8.1951,-65.4314},
        {4.2624,4.6707,8.8582,-67.1914},
        {4.3596,4.8607,9.4965,-68.8957},
        {4.4609,5.0618,10.0536,-70.5439},
        {4.5662,5.2683,10.3268,-72.1347},
        {4.6717,5.2718,0.1756,-73.6416},
        {4.7756,5.193,-3.9383,-75.0332},
        {4.8778,5.1141,-3.9457,-76.308},
        {4.9783,5.027,-4.3557,-77.4768},
        {5.077,4.9304,-4.8276,-78.5502},
        {5.1735,4.8261,-5.2157,-79.537},
        {5.2678,4.7144,-5.5842,-80.4448},
        {5.3598,4.5973,-5.8556,-81.2812},
        {5.4493,4.4739,-6.1696,-82.0528},
        {5.5361,4.3452,-6.4377,-82.7647},
        {5.6204,4.2131,-6.6014,-83.4224},
        {5.7019,4.0761,-6.8493,-84.0307},
        {5.7807,3.936,-7.007,-84.5933},
        {5.8565,3.7924,-7.181,-85.1139},
        {5.9295,3.6466,-7.2824,-85.5959},
        {5.9993,3.4972,-7.4778,-86.0419},
        {6.0664,3.3471,-7.501,-86.4546},
        {6.1302,3.1935,-7.6836,-86.8365},
        {6.191,3.039,-7.7249,-87.1892},
        {6.2486,2.8827,-7.8175,-87.5148},
        {6.3031,2.7249,-7.8875,-87.8152},
        {6.3545,2.5657,-7.949,-88.0917},
        {6.4026,2.4051,-8.0255,-88.3457},
        {6.4474,2.2438,-8.0849,-88.5778},
        {6.4891,2.0822,-8.0664,-88.7896},
        {6.5275,1.9187,-8.1733,-88.9822},
        {6.5625,1.7549,-8.2054,-89.156},
        {6.5944,1.5909,-8.1904,-89.3119},
        {6.623,1.4255,-8.2456,-89.4509},
        {6.6481,1.2596,-8.3083,-89.5731},
        {6.6701,1.094,-8.2593,-89.6791},
        {6.6886,0.9272,-8.3419,-89.7695},
        {6.7038,0.7607,-8.3128,-89.8443},
        {6.7157,0.5934,-8.3381,-89.9043},
        {6.7243,0.4257,-8.3489,-89.9493},
        {6.7295,0.2576,-8.3551,-89.9796},
        {6.7313,0.084,-8.0805,-89.9953}
    };

}
