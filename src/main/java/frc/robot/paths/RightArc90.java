/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.paths;

/**
 * Add your docs here.
 */
public class RightArc90 extends Path {

    @Override
    public double[][] getLeftPath() {
        return this.RightArc90Left;
    }

    @Override
    public double[][] getRightPath() {
        return this.RightArc90Right;
    }

    public double[][] RightArc90Left = new double[][] {
        {0,0,0,0},
        {0.0021,0.1028,5.0961,0},
        {0.0081,0.3013,9.9698,0},
        {0.0181,0.5006,9.9882,0},
        {0.0321,0.7003,9.9946,0},
        {0.0501,0.9002,9.9917,0},
        {0.0721,1.1003,10.0041,0},
        {0.0981,1.3002,10.0002,0},
        {0.1281,1.5002,9.9966,0},
        {0.1621,1.7002,10.0049,0},
        {0.2,1.9001,9.9991,0},
        {0.2421,2.1001,9.9949,0},
        {0.288,2.3001,10.0055,0},
        {0.3381,2.5001,9.9966,0},
        {0.392,2.7,10.0021,0},
        {0.4501,2.9001,9.9956,0},
        {0.5121,3.1001,10.0053,0},
        {0.5781,3.3001,9.9967,0},
        {0.648,3.5001,10.0033,0},
        {0.7221,3.7,9.997,0},
        {0.8001,3.9,10.0013,0},
        {0.882,4.1,9.9999,0},
        {0.9681,4.3,9.9993,0},
        {1.0581,4.5,9.9994,0},
        {1.1521,4.7001,10.0001,0},
        {1.2501,4.9001,10.0014,0},
        {1.3521,5.1001,10.0006,0},
        {1.4581,5.3001,9.9981,0},
        {1.5681,5.5001,10.0012,0},
        {1.6821,5.7001,10,0},
        {1.8001,5.9,9.9996,0},
        {1.9221,6.1001,9.9998,0},
        {2.0481,6.3001,10.0008,0},
        {2.178,6.5,10.0003,0},
        {2.3121,6.7,9.9986,0},
        {2.4501,6.9,10,0},
        {2.5921,7.1001,10.0003,0},
        {2.738,7.3,10.0016,0},
        {2.888,7.5,9.9986,0},
        {3.0421,7.7,9.9991,0},
        {3.2001,7.9,10.0012,0},
        {3.3621,8.1,10.0001,0},
        {3.5281,8.3,9.9999,0},
        {3.6981,8.5,9.9995,0},
        {3.8721,8.7,9.9998,0},
        {4.05,8.9,10.0017,0},
        {4.2321,9.1,9.9983,0},
        {4.4163,9.2127,5.6351,0},
        {4.597,9.0357,-8.8487,0},
        {4.7737,8.8357,-9.9997,0},
        {4.9465,8.6357,-9.9995,0},
        {5.1201,8.6772,2.0756,0.2681},
        {5.3048,9.2405,28.1764,1.3712},
        {5.4879,9.1559,-4.2296,2.5592},
        {5.6685,9.0287,-6.3565,3.7766},
        {5.8467,8.9081,-6.0315,5.0233},
        {6.0226,8.7926,-5.7718,6.2999},
        {6.1962,8.6847,-5.4004,7.6062},
        {6.3678,8.5814,-5.1662,8.9424},
        {6.5375,8.4847,-4.8321,10.309},
        {6.7055,8.3936,-4.5547,11.7065},
        {6.8716,8.3095,-4.2087,13.1338},
        {7.0362,8.2291,-4.0169,14.5911},
        {7.1993,8.1563,-3.6397,16.0782},
        {7.3611,8.0874,-3.4436,17.5943},
        {7.5216,8.0247,-3.1347,19.1395},
        {7.6809,7.9677,-2.8519,20.7122},
        {7.8392,7.9145,-2.6598,22.3117},
        {7.9966,7.8671,-2.3696,23.9376},
        {8.153,7.825,-2.1075,25.5879},
        {8.3088,7.7861,-1.9411,27.2613},
        {8.4639,7.7532,-1.6495,28.9565},
        {8.6183,7.7236,-1.4774,30.6713},
        {8.7723,7.6987,-1.2448,32.404},
        {8.9259,7.6779,-1.042,34.1524},
        {9.0791,7.6605,-0.8689,35.9144},
        {9.232,7.6479,-0.6318,37.6875},
        {9.3848,7.6375,-0.5171,39.4694},
        {9.5374,7.6322,-0.2656,41.258},
        {9.69,7.6295,-0.1354,43.0504},
        {9.8426,7.6312,0.0865,44.8438},
        {9.9953,7.635,0.189,46.6359},
        {10.1482,7.6436,0.4319,48.4245},
        {10.3013,7.6548,0.5577,50.2067},
        {10.4547,7.6703,0.7775,51.98},
        {10.6084,7.688,0.8837,53.7423},
        {10.7626,7.7106,1.1297,55.4917},
        {10.9174,7.7359,1.2645,57.2259},
        {11.0727,7.7653,1.47,58.9431},
        {11.2287,7.7984,1.6559,60.6415},
        {11.3854,7.8353,1.8448,62.3192},
        {11.5429,7.8756,2.0156,63.9751},
        {11.7013,7.9202,2.2312,65.6081},
        {11.8607,7.9684,2.4073,67.2175},
        {12.0211,8.0211,2.6327,68.8021},
        {12.1793,7.9117,-5.4685,70.3433},
        {12.3322,7.647,-13.2371,71.8109},
        {12.4797,7.3733,-13.68,73.1943},
        {12.6218,7.1033,-13.5033,74.497},
        {12.7585,6.8361,-13.357,75.7232},
        {12.8899,6.5734,-13.1393,76.8765},
        {13.0162,6.3127,-13.0346,77.9609},
        {13.1374,6.056,-12.8294,78.9805},
        {13.2534,5.802,-12.6974,79.9383},
        {13.3644,5.5512,-12.5462,80.8373},
        {13.4705,5.3023,-12.4433,81.6806},
        {13.5716,5.0564,-12.2949,82.471},
        {13.6679,4.8121,-12.208,83.2113},
        {13.7593,4.5708,-12.0682,83.9037},
        {13.8459,4.3306,-12.0119,84.5502},
        {13.9277,4.0927,-11.8986,85.153},
        {14.0048,3.856,-11.8315,85.7139},
        {14.0773,3.6208,-11.7554,86.2349},
        {14.145,3.3875,-11.6712,86.717},
        {14.2081,3.1547,-11.6432,87.1617},
        {14.2666,2.923,-11.5685,87.571},
        {14.3205,2.6932,-11.4981,87.9452},
        {14.3697,2.4634,-11.496,88.2853},
        {14.4145,2.2344,-11.4344,88.5928},
        {14.4546,2.0066,-11.3855,88.8683},
        {14.4901,1.7796,-11.374,89.1122},
        {14.5212,1.5524,-11.3493,89.3254},
        {14.5477,1.3263,-11.302,89.5087},
        {14.5698,1.0997,-11.2908,89.6624},
        {14.5873,0.8739,-11.271,89.7868},
        {14.6002,0.6489,-11.2874,89.8819},
        {14.6088,0.4223,-11.2066,89.9484},
        {14.6128,0.1933,-11.1407,89.9864}
    };

    public double[][] RightArc90Right = new double[][] {
        {0,0,0,0},
        {0.0021,0.1028,5.0961,0},
        {0.0081,0.3013,9.9698,0},
        {0.0181,0.5006,9.9882,0},
        {0.0321,0.7003,9.9946,0},
        {0.0501,0.9002,9.9917,0},
        {0.0721,1.1003,10.0041,0},
        {0.0981,1.3002,10.0002,0},
        {0.1281,1.5002,9.9966,0},
        {0.1621,1.7002,10.0049,0},
        {0.2,1.9001,9.9991,0},
        {0.2421,2.1001,9.9949,0},
        {0.288,2.3001,10.0055,0},
        {0.3381,2.5001,9.9966,0},
        {0.392,2.7,10.0021,0},
        {0.4501,2.9001,9.9956,0},
        {0.5121,3.1001,10.0053,0},
        {0.5781,3.3001,9.9967,0},
        {0.648,3.5001,10.0033,0},
        {0.7221,3.7,9.997,0},
        {0.8001,3.9,10.0013,0},
        {0.882,4.1,9.9999,0},
        {0.9681,4.3,9.9993,0},
        {1.0581,4.5,9.9994,0},
        {1.1521,4.7001,10.0001,0},
        {1.2501,4.9001,10.0014,0},
        {1.3521,5.1001,10.0006,0},
        {1.4581,5.3001,9.9981,0},
        {1.5681,5.5001,10.0012,0},
        {1.6821,5.7001,10,0},
        {1.8001,5.9,9.9996,0},
        {1.9221,6.1001,9.9998,0},
        {2.0481,6.3001,10.0008,0},
        {2.178,6.5,10.0003,0},
        {2.3121,6.7,9.9986,0},
        {2.4501,6.9,10,0},
        {2.5921,7.1001,10.0003,0},
        {2.738,7.3,10.0016,0},
        {2.888,7.5,9.9986,0},
        {3.0421,7.7,9.9991,0},
        {3.2001,7.9,10.0012,0},
        {3.3621,8.1,10.0001,0},
        {3.5281,8.3,9.9999,0},
        {3.6981,8.5,9.9995,0},
        {3.8721,8.7,9.9998,0},
        {4.05,8.9,10.0017,0},
        {4.2321,9.1,9.9983,0},
        {4.4163,9.2127,5.6351,0},
        {4.597,9.0357,-8.8487,0},
        {4.7737,8.8357,-9.9997,0},
        {4.9465,8.6357,-9.9995,0},
        {5.1107,8.2094,-21.3088,0.2681},
        {5.2569,7.3146,-44.7615,1.3712},
        {5.3986,7.0826,-11.6006,2.5592},
        {5.5367,6.9047,-8.8909,3.7766},
        {5.6713,6.732,-8.6367,5.0233},
        {5.8027,6.5659,-8.3029,6.2999},
        {5.9307,6.404,-8.0975,7.6062},
        {6.0557,6.2493,-7.7374,8.9424},
        {6.1777,6.0996,-7.4851,10.309},
        {6.2969,5.9561,-7.1713,11.7065},
        {6.4132,5.8174,-6.9364,13.1338},
        {6.5269,5.6868,-6.5297,14.5911},
        {6.6381,5.5603,-6.3244,16.0782},
        {6.747,5.4418,-5.925,17.5943},
        {6.8536,5.3289,-5.6444,19.1395},
        {6.958,5.2221,-5.3399,20.7122},
        {7.0604,5.1234,-4.9371,22.3117},
        {7.1611,5.0306,-4.6373,23.9376},
        {7.2599,4.9444,-4.3115,25.5879},
        {7.3573,4.8667,-3.8845,27.2613},
        {7.4532,4.7949,-3.5888,28.9565},
        {7.5478,4.7315,-3.1709,30.6713},
        {7.6414,4.6752,-2.8161,32.404},
        {7.7339,4.6265,-2.4326,34.1524},
        {7.8256,4.5861,-2.0198,35.9144},
        {7.9166,4.5526,-1.6745,37.6875},
        {8.0072,4.5285,-1.2069,39.4694},
        {8.0974,4.5109,-0.8801,41.258},
        {8.1875,4.5022,-0.4339,43.0504},
        {8.2775,4.5006,-0.0828,44.8438},
        {8.3676,4.5082,0.3839,46.6359},
        {8.4581,4.5224,0.7067,48.4245},
        {8.549,4.5452,1.1415,50.2067},
        {8.6405,4.5748,1.4784,51.98},
        {8.7327,4.6132,1.9213,53.7423},
        {8.8259,4.6576,2.2209,55.4917},
        {8.9201,4.7101,2.623,57.2259},
        {9.0155,4.7691,2.9483,58.9431},
        {9.1122,4.8348,3.2853,60.6415},
        {9.2103,4.907,3.6105,62.3192},
        {9.31,4.9858,3.943,63.9751},
        {9.4114,5.0703,4.2221,65.6081},
        {9.5147,5.1608,4.5279,67.2175},
        {9.6198,5.2559,4.755,68.8021},
        {9.7242,5.2211,-1.7448,70.3433},
        {9.8259,5.0859,-6.7557,71.8109},
        {9.9251,4.9596,-6.3167,73.1943},
        {10.0217,4.8296,-6.4983,74.497},
        {10.1157,4.6968,-6.6372,75.7232},
        {10.2068,4.5596,-6.8678,76.8765},
        {10.2952,4.4204,-6.958,77.9609},
        {10.3808,4.277,-7.1694,78.9805},
        {10.4634,4.1309,-7.3005,79.9383},
        {10.543,3.9818,-7.459,80.8373},
        {10.6197,3.8307,-7.5525,81.6806},
        {10.6932,3.6766,-7.7081,82.471},
        {10.7636,3.5208,-7.7835,83.2113},
        {10.8309,3.3621,-7.9403,83.9037},
        {10.8949,3.2024,-7.9837,84.5502},
        {10.9557,3.0403,-8.1041,85.153},
        {11.0132,2.877,-8.1672,85.7139},
        {11.0675,2.7121,-8.2375,86.2349},
        {11.1184,2.5454,-8.3425,86.717},
        {11.1659,2.3784,-8.3505,87.1617},
        {11.2102,2.2099,-8.4146,87.571},
        {11.251,2.0395,-8.5252,87.9452},
        {11.2883,1.8696,-8.5016,88.2853},
        {11.3224,1.6984,-8.548,88.5928},
        {11.3529,1.5259,-8.6238,88.8683},
        {11.3799,1.3532,-8.6473,89.1122},
        {11.4035,1.1806,-8.6239,89.3254},
        {11.4237,1.0065,-8.704,89.5087},
        {11.4404,0.8323,-8.6796,89.6624},
        {11.4535,0.6571,-8.7456,89.7868},
        {11.4631,0.4825,-8.762,89.8819},
        {11.4694,0.3075,-8.6496,89.9484},
        {11.472,0.1287,-8.7002,89.9864}
    };

}