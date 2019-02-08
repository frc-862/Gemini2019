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
public class TenFtPath extends Path {
    public static double[][] Left = new double[][] {
        {0,0,0,0},
        {0.0008,0.042,2.0908,0},
        {0.0033,0.1206,3.9037,0},
        {0.0073,0.2007,4.0195,0},
        {0.0129,0.2804,4.0032,0},
        {0.0201,0.3603,3.9869,0},
        {0.0288,0.4402,4.0107,0},
        {0.0393,0.5201,3.9921,0},
        {0.0513,0.6002,4,0},
        {0.0648,0.6801,4.0045,0},
        {0.08,0.7601,3.9969,0},
        {0.0969,0.8401,3.9968,0},
        {0.1152,0.9201,4.0065,0},
        {0.1352,1,3.9968,0},
        {0.1569,1.08,3.998,0},
        {0.1801,1.1601,3.9996,0},
        {0.2048,1.2401,4.0055,0},
        {0.2313,1.32,3.9958,0},
        {0.2593,1.4001,3.9989,0},
        {0.2889,1.4801,4.0022,0},
        {0.32,1.5601,4.0019,0},
        {0.3529,1.6401,3.9951,0},
        {0.3873,1.7201,4.0029,0},
        {0.4233,1.8001,4.0003,0},
        {0.4608,1.88,4.0014,0},
        {0.5001,1.96,3.9962,0},
        {0.5409,2.0401,4.0011,0},
        {0.5833,2.1201,4.0028,0},
        {0.6272,2.2,3.9983,0},
        {0.6729,2.2801,3.9972,0},
        {0.72,2.3601,4.0054,0},
        {0.7689,2.44,3.9954,0},
        {0.8193,2.5201,4.0007,0},
        {0.8713,2.6001,4.003,0},
        {0.9249,2.6801,3.9965,0},
        {0.9801,2.7601,4.0019,0},
        {1.0368,2.84,4.0014,0},
        {1.0953,2.92,3.9982,0},
        {1.1553,3.0001,3.9979,0},
        {1.2169,3.0801,4.0033,0},
        {1.2801,3.16,4.0002,0},
        {1.3449,3.2401,3.9973,0},
        {1.4113,3.3201,4.0027,0},
        {1.4793,3.4001,3.9971,0},
        {1.5489,3.4801,4.0025,0},
        {1.6201,3.5601,3.9997,0},
        {1.6929,3.6401,3.9997,0},
        {1.7673,3.7201,3.9997,0},
        {1.8433,3.8001,3.9998,0},
        {1.9209,3.8801,3.9998,0},
        {2.0001,3.9601,4.0025,0},
        {2.0809,4.04,3.9975,0},
        {2.1633,4.1201,4.0002,0},
        {2.2473,4.2001,4.0003,0},
        {2.3329,4.2801,4.0005,0},
        {2.42,4.36,4.0007,0},
        {2.5089,4.44,3.9984,0},
        {2.5992,4.52,4.0011,0},
        {2.6913,4.6,3.9989,0},
        {2.7849,4.6801,3.9992,0},
        {2.8801,4.7601,4.0018,0},
        {2.9769,4.8401,3.9998,0},
        {3.0753,4.9201,4.0001,0},
        {3.1753,5,4.0004,0},
        {3.2769,5.08,3.9985,0},
        {3.3801,5.1601,4.0011,0},
        {3.4849,5.2401,3.9993,0},
        {3.5912,5.32,4.0019,0},
        {3.6993,5.4,3.9979,0},
        {3.8089,5.48,4.0006,0},
        {3.9201,5.5601,3.9989,0},
        {4.0329,5.6401,4.0016,0},
        {4.1473,5.7201,4,0},
        {4.2632,5.8,4.0005,0},
        {4.3808,5.88,3.999,0},
        {4.5001,5.96,3.9997,0},
        {4.6209,6.04,4.0004,0},
        {4.7433,6.12,3.999,0},
        {4.8673,6.2,4.0018,0},
        {4.9929,6.28,3.9986,0},
        {5.1187,6.2887,0.4334,0},
        {5.2429,6.2089,-3.9868,0},
        {5.3654,6.1289,-4.0021,0},
        {5.4864,6.049,-3.9989,0},
        {5.6058,5.969,-3.9997,0},
        {5.7235,5.889,-4.0005,0},
        {5.8397,5.809,-3.9991,0},
        {5.9544,5.7289,-3.9997,0},
        {6.0674,5.6489,-4.0004,0},
        {6.1787,5.5689,-4.001,0},
        {6.2885,5.4889,-3.9993,0},
        {6.3967,5.4089,-3.9998,0},
        {6.5033,5.3289,-4.0003,0},
        {6.6082,5.2489,-4.0008,0},
        {6.7116,5.1689,-3.9989,0},
        {6.8134,5.0889,-3.9993,0},
        {6.9135,5.0089,-4.002,0},
        {7.0122,4.9289,-3.9976,0},
        {7.1091,4.8489,-4.0027,0},
        {7.2045,4.7689,-3.9981,0},
        {7.2982,4.6889,-4.0008,0},
        {7.3905,4.6089,-3.9985,0},
        {7.481,4.5289,-4.0012,0},
        {7.5701,4.4489,-3.9988,0},
        {7.6574,4.3689,-4.0016,0},
        {7.7432,4.2889,-3.9991,0},
        {7.8273,4.2089,-4.0018,0},
        {7.91,4.1289,-3.9965,0},
        {7.9909,4.0489,-4.0018,0},
        {8.0702,3.9689,-4.0019,0},
        {8.1481,3.8889,-3.9963,0},
        {8.2243,3.8089,-4.0018,0},
        {8.2988,3.7289,-4.0017,0},
        {8.3719,3.6489,-3.9958,0},
        {8.4432,3.5689,-4.0043,0},
        {8.5129,3.489,-3.9982,0},
        {8.5812,3.4089,-3.9979,0},
        {8.6477,3.3289,-4.0036,0},
        {8.7127,3.249,-3.9971,0},
        {8.7761,3.1689,-3.9997,0},
        {8.8379,3.0889,-4.0025,0},
        {8.898,3.0089,-3.9988,0},
        {8.9567,2.9289,-3.9981,0},
        {9.0136,2.8489,-4.0041,0},
        {9.069,2.769,-3.9967,0},
        {9.1228,2.6889,-3.9992,0},
        {9.1749,2.6089,-4.0019,0},
        {9.2255,2.529,-4.001,0},
        {9.2745,2.449,-3.9963,0},
        {9.3219,2.369,-4.0025,0},
        {9.3677,2.2889,-3.9975,0},
        {9.4118,2.209,-4.0039,0},
        {9.4545,2.1289,-3.9946,0},
        {9.4954,2.0489,-4.005,0},
        {9.5347,1.969,-3.9994,0},
        {9.5725,1.889,-3.9975,0},
        {9.6087,1.809,-3.9998,0},
        {9.6434,1.7289,-3.9976,0},
        {9.6763,1.6489,-4.0048,0},
        {9.7077,1.5689,-3.9977,0},
        {9.7375,1.4889,-4,0},
        {9.7656,1.409,-4.0027,0},
        {9.7922,1.329,-3.9942,0},
        {9.8172,1.2489,-4.0022,0},
        {9.8406,1.1688,-3.999,0},
        {9.8624,1.0888,-4.002,0},
        {9.8826,1.0088,-3.9984,0},
        {9.9011,0.9288,-4.0018,0},
        {9.9181,0.8488,-3.9977,0},
        {9.9335,0.7688,-4.0019,0},
        {9.9473,0.6888,-3.9974,0},
        {9.9594,0.6088,-4.0032,0},
        {9.97,0.5288,-3.9989,0},
        {9.979,0.4487,-3.9922,0},
        {9.9864,0.3684,-4.0007,0},
        {9.9921,0.2886,-4.0214,0},
        {9.9964,0.2085,-3.9624,0},
        {9.9989,0.1276,-4.0007,0},
        {9.9999,0.0436,-3.8557,0}
    };

    public static double[][] Right = new double[][] {
        {0,0,0,0},
        {0.0008,0.042,2.0908,0},
        {0.0033,0.1206,3.9037,0},
        {0.0073,0.2007,4.0195,0},
        {0.0129,0.2804,4.0032,0},
        {0.0201,0.3603,3.9869,0},
        {0.0288,0.4402,4.0107,0},
        {0.0393,0.5201,3.9921,0},
        {0.0513,0.6002,4,0},
        {0.0648,0.6801,4.0045,0},
        {0.08,0.7601,3.9969,0},
        {0.0969,0.8401,3.9968,0},
        {0.1152,0.9201,4.0065,0},
        {0.1352,1,3.9968,0},
        {0.1569,1.08,3.998,0},
        {0.1801,1.1601,3.9996,0},
        {0.2048,1.2401,4.0055,0},
        {0.2313,1.32,3.9958,0},
        {0.2593,1.4001,3.9989,0},
        {0.2889,1.4801,4.0022,0},
        {0.32,1.5601,4.0019,0},
        {0.3529,1.6401,3.9951,0},
        {0.3873,1.7201,4.0029,0},
        {0.4233,1.8001,4.0003,0},
        {0.4608,1.88,4.0014,0},
        {0.5001,1.96,3.9962,0},
        {0.5409,2.0401,4.0011,0},
        {0.5833,2.1201,4.0028,0},
        {0.6272,2.2,3.9983,0},
        {0.6729,2.2801,3.9972,0},
        {0.72,2.3601,4.0054,0},
        {0.7689,2.44,3.9954,0},
        {0.8193,2.5201,4.0007,0},
        {0.8713,2.6001,4.003,0},
        {0.9249,2.6801,3.9965,0},
        {0.9801,2.7601,4.0019,0},
        {1.0368,2.84,4.0014,0},
        {1.0953,2.92,3.9982,0},
        {1.1553,3.0001,3.9979,0},
        {1.2169,3.0801,4.0033,0},
        {1.2801,3.16,4.0002,0},
        {1.3449,3.2401,3.9973,0},
        {1.4113,3.3201,4.0027,0},
        {1.4793,3.4001,3.9971,0},
        {1.5489,3.4801,4.0025,0},
        {1.6201,3.5601,3.9997,0},
        {1.6929,3.6401,3.9997,0},
        {1.7673,3.7201,3.9997,0},
        {1.8433,3.8001,3.9998,0},
        {1.9209,3.8801,3.9998,0},
        {2.0001,3.9601,4.0025,0},
        {2.0809,4.04,3.9975,0},
        {2.1633,4.1201,4.0002,0},
        {2.2473,4.2001,4.0003,0},
        {2.3329,4.2801,4.0005,0},
        {2.42,4.36,4.0007,0},
        {2.5089,4.44,3.9984,0},
        {2.5992,4.52,4.0011,0},
        {2.6913,4.6,3.9989,0},
        {2.7849,4.6801,3.9992,0},
        {2.8801,4.7601,4.0018,0},
        {2.9769,4.8401,3.9998,0},
        {3.0753,4.9201,4.0001,0},
        {3.1753,5,4.0004,0},
        {3.2769,5.08,3.9985,0},
        {3.3801,5.1601,4.0011,0},
        {3.4849,5.2401,3.9993,0},
        {3.5912,5.32,4.0019,0},
        {3.6993,5.4,3.9979,0},
        {3.8089,5.48,4.0006,0},
        {3.9201,5.5601,3.9989,0},
        {4.0329,5.6401,4.0016,0},
        {4.1473,5.7201,4,0},
        {4.2632,5.8,4.0005,0},
        {4.3808,5.88,3.999,0},
        {4.5001,5.96,3.9997,0},
        {4.6209,6.04,4.0004,0},
        {4.7433,6.12,3.999,0},
        {4.8673,6.2,4.0018,0},
        {4.9929,6.28,3.9986,0},
        {5.1187,6.2887,0.4334,0},
        {5.2429,6.2089,-3.9868,0},
        {5.3654,6.1289,-4.0021,0},
        {5.4864,6.049,-3.9989,0},
        {5.6058,5.969,-3.9997,0},
        {5.7235,5.889,-4.0005,0},
        {5.8397,5.809,-3.9991,0},
        {5.9544,5.7289,-3.9997,0},
        {6.0674,5.6489,-4.0004,0},
        {6.1787,5.5689,-4.001,0},
        {6.2885,5.4889,-3.9993,0},
        {6.3967,5.4089,-3.9998,0},
        {6.5033,5.3289,-4.0003,0},
        {6.6082,5.2489,-4.0008,0},
        {6.7116,5.1689,-3.9989,0},
        {6.8134,5.0889,-3.9993,0},
        {6.9135,5.0089,-4.002,0},
        {7.0122,4.9289,-3.9976,0},
        {7.1091,4.8489,-4.0027,0},
        {7.2045,4.7689,-3.9981,0},
        {7.2982,4.6889,-4.0008,0},
        {7.3905,4.6089,-3.9985,0},
        {7.481,4.5289,-4.0012,0},
        {7.5701,4.4489,-3.9988,0},
        {7.6574,4.3689,-4.0016,0},
        {7.7432,4.2889,-3.9991,0},
        {7.8273,4.2089,-4.0018,0},
        {7.91,4.1289,-3.9965,0},
        {7.9909,4.0489,-4.0018,0},
        {8.0702,3.9689,-4.0019,0},
        {8.1481,3.8889,-3.9963,0},
        {8.2243,3.8089,-4.0018,0},
        {8.2988,3.7289,-4.0017,0},
        {8.3719,3.6489,-3.9958,0},
        {8.4432,3.5689,-4.0043,0},
        {8.5129,3.489,-3.9982,0},
        {8.5812,3.4089,-3.9979,0},
        {8.6477,3.3289,-4.0036,0},
        {8.7127,3.249,-3.9971,0},
        {8.7761,3.1689,-3.9997,0},
        {8.8379,3.0889,-4.0025,0},
        {8.898,3.0089,-3.9988,0},
        {8.9567,2.9289,-3.9981,0},
        {9.0136,2.8489,-4.0041,0},
        {9.069,2.769,-3.9967,0},
        {9.1228,2.6889,-3.9992,0},
        {9.1749,2.6089,-4.0019,0},
        {9.2255,2.529,-4.001,0},
        {9.2745,2.449,-3.9963,0},
        {9.3219,2.369,-4.0025,0},
        {9.3677,2.2889,-3.9975,0},
        {9.4118,2.209,-4.0039,0},
        {9.4545,2.1289,-3.9946,0},
        {9.4954,2.0489,-4.005,0},
        {9.5347,1.969,-3.9994,0},
        {9.5725,1.889,-3.9975,0},
        {9.6087,1.809,-3.9998,0},
        {9.6434,1.7289,-3.9976,0},
        {9.6763,1.6489,-4.0048,0},
        {9.7077,1.5689,-3.9977,0},
        {9.7375,1.4889,-4,0},
        {9.7656,1.409,-4.0027,0},
        {9.7922,1.329,-3.9942,0},
        {9.8172,1.2489,-4.0022,0},
        {9.8406,1.1688,-3.999,0},
        {9.8624,1.0888,-4.002,0},
        {9.8826,1.0088,-3.9984,0},
        {9.9011,0.9288,-4.0018,0},
        {9.9181,0.8488,-3.9977,0},
        {9.9335,0.7688,-4.0019,0},
        {9.9473,0.6888,-3.9974,0},
        {9.9594,0.6088,-4.0032,0},
        {9.97,0.5288,-3.9989,0},
        {9.979,0.4487,-3.9922,0},
        {9.9864,0.3684,-4.0007,0},
        {9.9921,0.2886,-4.0214,0},
        {9.9964,0.2085,-3.9624,0},
        {9.9989,0.1276,-4.0007,0},
        {9.9999,0.0436,-3.8557,0}
    };
}
