package com.lc.utils;

import org.apache.poi.ss.formula.functions.Finance;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 金融计算类 1.等额本息还款 2.预期收益 3.加权平均收益率, 4.利息
 *
 * 逾期还款:每天的利息，罚息，平台账号管理服务费 债权转让：债权转让手续费 满标放款手续费,逾期垫付的罚息,提现手续费,提前清贷手续费
 *
 * 参与者:借款人mortgagor O 投资人:investor I 平台:platform P ; 汇付天下,第三方平台: TP
 *
 * O2I 借款人-->投资人,O2P 借款人-->平台,I2P 投资人-->平台,P2T 平台-->第三方
 *
 * @author ChenChang
 */
/**
 * @author ChenChang
 *
 */

/**
 * @author ChenChang
 *
 */

public class FinanceUtil {

	/**
	 * 计算平台收益
	 *
	 * @param r
	 * @param nper
	 * @param pv
	 * @param isRoll
	 * @return
	 */
	public static double calcYield4P2p(double r, int nper, double pv, boolean isRoll) {
		double y = 0;
		if (isRoll) {
			y = calcYieldBase(r, nper, pv);
		} else {
			y = calcYield4P2pPmt(r, nper, pv);
		}
		return Math.abs(y);
	}

	/**
	 * 计算收益_基础方法(本金+利息)
	 *
	 * @param r
	 * @param nper
	 * @param pv
	 * @return
	 */
	public static double calcYieldBase(double r, int nper, double pv) {
		return pv + pv * nper * r;
	}

	/**
	 * 计算利息_基础方法(月利息)
	 *
	 * @param r
	 * @param pv
	 * @return
	 */
	public static double calcMonthInterest(double r, double pv) {
		return round2(calcFeeBase(pv, r));
	}

	/**
	 * 收益
	 *
	 * @param r
	 *            月利率
	 * @param nper
	 *            期数
	 * @param pv
	 *            本金
	 * @return
	 */
	public static double calcYield4P2pPmt(double r, int nper, double pv) {
		return FinanceUtil.PMT_ALL(r, nper, pv);
	}

	public static double round2(double value) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		return Double.valueOf(df.format(value));
	}

	// 等额本息还款 BEGIN

	/**
	 * 等额本息还款: 每期应还本息(本金+利息)
	 *
	 * 普通期数,非最后一期
	 *
	 * @param r
	 *            月利率
	 * @param nper
	 *            总期数
	 * @param pv
	 *            本金
	 * @return
	 */
	public static double PMT(double r, int nper, double pv) {
		return round2(Math.abs(Finance.pmt(r, nper, pv)));
	}

	/**
	 * 等额本息还款 每期应还本息
	 *
	 * @param r
	 * @param per
	 * @param nper
	 * @param pv
	 * @return
	 */
	public static double PMT(double r, int per, int nper, double pv) {
		double value = 0.0;
		if (per != nper) {
			value = PMT(r, nper, pv);
		} else {
			value = calcPmtBase(r, per, nper, pv, "PMT");
		}
		return value;
	}

	/**
	 * 等额本息还款 总额
	 *
	 * @param r
	 * @param nper
	 * @param pv
	 * @return
	 */
	public static double PMT_ALL(double r, int nper, double pv) {
		return calcPmtBase(r, nper, nper, pv, "PMT_ALL");
	}

	public static double calcPmtBase(double r, int per, int nper, double pv, String retType) {
		double retValue = 0;
		double total = 0;
		//double pmt = pv * r * Math.pow((1 + r), nper) / (Math.pow((1 + r), nper) - 1);
		double pmt = PMT(r, nper, pv);
		pmt = round2(pmt);
		double ipmt = 0;
		double ppmt = 0;

		for (int i = 1; i <= per; i++) {

			ipmt = round2(pv * r); //利息
			if (i == nper) {
				pmt = round2(pv * (1 + r)); //应还本息
				ppmt = pv; //应还本金
				pv = 0;
			} else {
				ppmt = pmt - ipmt;
				pv = pv - ppmt;
			}
			//	System.out.println("第 " + i + " 期还" + perMonth + "");
			total += pmt;
		}

		if ("PMT_ALL".equals(retType))
			retValue = total;
		else if ("PMT".equals(retType))
			retValue = pmt;
		else if ("IPMT".equals(retType))
			retValue = ipmt;
		else if ("PPMT".equals(retType))
			retValue = ppmt;
		else if ("LP".equals(retType))
			retValue = pv;
		else
			retValue = 0;

		return round2(retValue);
	}

	/**
	 * 等额本息还款: 第几期应还利息
	 *
	 * @param r
	 *            月利率
	 * @param per
	 *            第几期
	 * @param nper
	 *            总期数
	 * @param pv
	 *            本金
	 * @return
	 */
	public static double IPMT(double r, int per, int nper, double pv) {
		return calcPmtBase(r, per, nper, pv, "IPMT");
	}

	/**
	 * 等额本息还款: 第几期应还本金
	 *
	 *
	 *
	 *
	 *
	 * @param r
	 *            月利率
	 * @param per
	 *            第几期
	 * @param nper
	 *            总期数
	 * @param pv
	 *            本金
	 * @return
	 */
	public static double PPMT(double r, int per, int nper, double pv) {
		return calcPmtBase(r, per, nper, pv, "PPMT");
	}

	//	/**
	//	 * 等额本息还款: 第几期剩余本金
	//	 *
	//	 * FIRST: PV - P1
	//	 *
	//	 * ELSE: PRE PV - P2
	//	 *
	//	 * @param r
	//	 *            月利率
	//	 * @param per
	//	 *            第几期
	//	 * @param nper
	//	 *            总期数
	//	 * @param pv
	//	 *            本金
	//	 * @return
	//	 */
	public static double LP(double r, int per, int nper, double pv) {
		return calcPmtBase(r, per, nper, pv, "LP");
	}

	// 等额本息还款 END

	/**
	 * 利息
	 *
	 * @param r
	 *            日利率
	 * @param pv
	 *            本金
	 * @param days
	 *            天数
	 * @return
	 */
	public static double calcInterest(double r, double pv, int days) {
		return pv * r * days;
	}

	/**
	 * 加权平均收益率
	 *
	 * @param hm
	 *            债权键值表
	 * @return
	 */
	public static double calcWeightedAvgYieldRate(Map<Integer, Double> hm) {
		double yieldRate = 0;
		double sumDebtRate = 0;
		int sumDebt = 0;

		Iterator<Entry<Integer, Double>> iter = hm.entrySet().iterator();
		Entry<Integer, Double> entry;
		int key = 0;
		double value = 0;

		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			value = entry.getValue();
			sumDebtRate += key * value;
			sumDebt += key;
		}

		if (sumDebt != 0) {
			yieldRate = sumDebtRate / sumDebt;
		}
		return yieldRate;
	}

	// 逾期还款 每天的利息 罚息
	// 逾期还款 每天的罚息
	// 逾期垫付的罚息
	// 每日0.05% 应还款额×0.05%每日
	/**
	 * 罚息
	 *
	 * 借款人--> 投资人
	 *
	 * @param p
	 *            应还款额
	 * @param r
	 *            费率
	 * @param days
	 *            天数
	 * @return
	 */
	public static double calcO2I_PenaltyInterest(double p, double r, int days) {
		return round2(p * r * days);
	}

	// 提前清贷补偿金
	// 一次性1% 剩余本金×1%
	/**
	 * 提前清贷补偿金
	 *
	 * 借款人--> 投资人
	 *
	 * @param p
	 *            剩余本金
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcO2I_PrePayoffFee(double p, double r) {
		return calcFeeBase(p, r);
	}

	private static double calcFeeBase(double p, double r) {
		return round2(p * r);
	}

	// 平台账号管理服务费 =本金×0.2%
	// 每月0.2%(暂定) 本金×0.2%

	/**
	 * 平台管理费
	 *
	 * 借款人--> P2P平台收费账户
	 *
	 * @param p
	 *            本金
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcO2P_PlatformManagementFee(double p, double r) {
		return calcFeeBase(p, r);
	}

	// 放款手续费 = 贷款金额 * 手续费率
	/**
	 * 放款手续费
	 *
	 * 借款人资金账户-->平台资金账户
	 *
	 * @param p
	 *            贷款金额
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcO2P_MakeLoanFee(double p, double r) {
		return calcFeeBase(p, r);
	}
	/**
	 * 履约保证金
	 *
	 * 借款人资金账户-->平台资金账户
	 *
	 * @param p
	 *            贷款金额
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcO2P_MakeBailFee(double p, double r) {
		return calcFeeBase(p, r);
	}
	// 风险备用金 每月0.4%~0.7%（暂定） 本金×风险备用金
	/**
	 * 风险备用金
	 *
	 * 借款人--> 风险备用金账户
	 *
	 * @param p
	 *            本金
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcO2P_RiskReserveFund(double p, double r) {
		return calcFeeBase(p, r);
	}

	// 投资管理费 10%的利息收入，罚息不计 利息收入×10%
	// 投资人 -->P2P平台收费账户
	/**
	 * 投资管理费
	 *
	 * 投资人 -->P2P平台收费账户
	 *
	 * @param p
	 *            利息收入
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcI2P_InvestmentManagementFee(double p, double r) {
		return calcFeeBase(p, r);
	}

	// 提现手续费（投资用户） 0.3%且大于2元 提现金额×0.3%
	/**
	 * 提现手续费（投资用户）
	 *
	 * 投资人--> P2P平台收费账户
	 *
	 * @param p
	 *            提现金额
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcI2P_WithdrawFee(double p, double r) {
		return calcFeeBase(p, r) < 2 ? 2 : calcFeeBase(p, r);
	}

	// 债权转让手续费
	// 持有债权3个月内1% 转让金额×债权转让手续费
	// 持有债权6个月内0.5%
	// 持有债权6个月以上免费
	/**
	 * 债权转让手续费
	 *
	 * 投资人--> P2P平台收费账户
	 *
	 * @param p
	 *            转让金额
	 * @param r
	 *            费率
	 * @return
	 */
	public static double calcI2P_ClmTransferFee(double p, double r) {
		return calcFeeBase(p, r);
	}

	// 充值费 工、农、建、中信、华夏按充值费率的1.5‰
	// 其余银行按充值费率的2.5‰
	// 企业充值10元/笔
	// 充值/提现手续费账户（名称暂定Vcredit4）--> 汇付天下
	/**
	 * 充值费
	 *
	 * 充值费_企业10元/笔
	 *
	 * @param p
	 *            充值金额
	 * @param r
	 *            费率
	 * @param isEnterprise
	 *            是否是企业充值
	 * @return
	 */
	public static double calcP2T_RechargeFee(double p, double r, boolean isEnterprise) {
		double ret = 0;
		if (!isEnterprise) {
			ret = calcFeeBase(p, r);
		} else {
			ret = 10;
		}
		return ret;
	}

	// 提现费 投资人取现2元/笔，
	// 借款人放款取现按取现费的0.5‰+2元/笔 本金×0.5‰+2元 或2元
	// 充值/提现手续费账户（名称暂定Vcredit4）--> 汇付天下
	/**
	 * 提现费
	 *
	 * 充值/提现手续费账户（名称暂定Vcredit4）--> 汇付天下
	 *
	 * @return
	 */
	public static double calcP2T_WithdrawFee(double p, double r, boolean isInvestor) {
		double ret = 0;
		if (isInvestor) {
			ret = 2;
		} else {
			ret = calcFeeBase(p, r) + 2;
		}
		return ret;
	}

	//	public static void main(String[] args) {
	//		int pv = 50;
	//		int nper = 24;
	//		double r = 0.13 / 12;
	//
	//		//int i = 2;
	//		//		System.out.print(FinanceUtil.PMT(r, i, nper, pv) + "        ");
	//		//System.out.print(FinanceUtil.IPMT(r, i, nper, pv) + "        ");
	//		//		System.out.print(FinanceUtil.PPMT(r, i, nper, pv) + "        ");
	//		//		System.out.println(FinanceUtil.LP(r, i, nper, pv));
	//		//		System.out.println(FinanceUtil.PMT_ALL(r, nper, pv));
	//
	//		//		i = 12;
	//		//		System.out.print(FinanceUtil.PMT(r, i, nper, pv) + "        ");
	//		//		System.out.print(FinanceUtil.IPMT(r, i, nper, pv) + "        ");
	//		//		System.out.print(FinanceUtil.PPMT(r, i, nper, pv) + "        ");
	//		//		System.out.println(FinanceUtil.LP(r, i, nper, pv));
	//		//		System.out.println(FinanceUtil.PMT_ALL(r, nper, pv));
	//
	//		System.out.println(FinanceUtil.PMT_ALL(r, nper, pv));
	//
	//		for (int j = 1; j <= nper; j++) {
	//			System.out.print(FinanceUtil.PMT(r, j, nper, pv) + "        ");
	//			System.out.print(FinanceUtil.IPMT(r, j, nper, pv) + "        ");
	//			System.out.print(FinanceUtil.PPMT(r, j, nper, pv) + "        ");
	//			System.out.println(FinanceUtil.LP(r, j, nper, pv));
	//		}
	//	}

}
