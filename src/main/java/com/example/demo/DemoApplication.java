package com.example.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.TB_COINDESK;
import com.example.demo.repository.TB_COINDESKRepository;
import com.google.gson.Gson;

@SpringBootApplication
public class DemoApplication {

//	@Autowired
//	public TB_COINDESKService tb_coindeskService;

	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DemoApplication.class,
				args);

		TB_COINDESKRepository tb_coindeskrepository = configurableApplicationContext
				.getBean(TB_COINDESKRepository.class);

		DemoApplication restUtil = new DemoApplication();

		String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

		String json = "";
		try {
			json = restUtil.getMethod(url);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}

		JSONObject object = JSONObject.parseObject(json);
		String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteDateUseDateFormat);
		System.out.println(pretty);

		System.setProperty("java.awt.headless", "false");

		JFrame jf = new JFrame("國泰世華JAVA engineer線上作業");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jf.setBounds(500, 500, 500, 500); // 設置窗口大小和位置
		JPanel panel = new JPanel();
		jf.setContentPane(panel); // 設定MyFrame視窗的容器為contentPane
		panel.setLayout(null); // 設定contentPane物件不使用版面配置管理者

		// 建立按鈕物件

		JButton btn0 = new JButton("coindesk");
		btn0.setBounds(20, 50, 100, 23);

		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 測試呼叫coindesk API，並顯示其內容
				JOptionPane.showMessageDialog(jf, pretty);
			}
		});
		panel.add(btn0);

		JButton btn1 = new JButton("資料轉換");
		btn1.setBounds(20, 100, 100, 23);

		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 建立資料庫讀取的API並資料轉換
				// JSON切割
				data s = new Gson().fromJson(pretty, data.class); // conversion using Gson Library.
				System.out.println(s.getChartName());
				System.out.println(s.getDisclaimer());
				String inf = "";
				String cd = "";
				String time = "";
				for (Map.Entry<String, data.bpilogin> entry : s.getBpi().entrySet()) {
					System.out.println("key:" + entry.getKey() + ",Code:" + entry.getValue().getCode() + ",Description:"
							+ entry.getValue().getDescription() + ",Rate:" + entry.getValue().getRate() + ",Rate_float:"
							+ entry.getValue().getRate_float() + ",Symbol:" + entry.getValue().getSymbol());
					cd = StringUtils.leftPad(matchCurcd(entry.getValue().getCode()), 16);
					time = s.getTime().getUpdatedISO().substring(0, 10).replace("-", "/") + " "
							+ s.getTime().getUpdatedISO().substring(11, 19);
					inf = inf + entry.getKey() + "     " + cd + "                " + entry.getValue().getRate()
							+ "         " + time;
					inf = inf + "\n";

					// 寫入h2 db
					TB_COINDESK tTB_COINDESK = new TB_COINDESK();
					tTB_COINDESK.setCURCD(entry.getKey());
					tTB_COINDESK.setNAME(cd);
					tTB_COINDESK.setRATE(entry.getValue().getRate());
					tTB_COINDESK.setCREATEDATE(time);
					tTB_COINDESK.setLASTUPDATEDATE(time);
					tb_coindeskrepository.save(tTB_COINDESK);

				}
				System.out.println(s.getTime().getUpdatedISO());
				// 差日期格式
				JOptionPane.showMessageDialog(jf, "幣別" + "     " + "幣別中文名稱" + "                " + "匯率"
						+ "                                " + "更新時間" + "\n" + inf);

			}

		});

		panel.add(btn1);

		JButton btn2 = new JButton("查詢");
		btn2.setBounds(20, 150, 100, 23);

		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 查詢db 回傳資料list開啟新視窗顯示資料
				List<TB_COINDESK> lTB_COINDESK = new ArrayList<TB_COINDESK>();
				lTB_COINDESK = tb_coindeskrepository.findAll();
				if (lTB_COINDESK != null && !lTB_COINDESK.isEmpty()) {

					String inf = "";
					String cd = "";
					String time = "";
					for (TB_COINDESK t : lTB_COINDESK) {
						cd = StringUtils.leftPad(t.getNAME(), 16);
						time = t.getLASTUPDATEDATE();
						inf = inf + StringUtils.leftPad(t.getCURCD(), 3) + "     " + cd + "                "
								+ StringUtils.leftPad(t.getRATE(), 11) + "         " + time;
						inf = inf + "\n";

					}

					JOptionPane.showMessageDialog(jf, "幣別" + "     " + "幣別中文名稱" + "                " + "匯率"
							+ "                                " + "更新時間" + "\n" + inf);
				} else {
					JOptionPane.showMessageDialog(jf, "查無資料");
				}
			}
		});
		panel.add(btn2);

		JButton btn3 = new JButton("新增");
		btn3.setBounds(20, 200, 100, 23);

		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 開啟新視窗顯示輸入新api的資料 輸入完成關閉

				JOptionPane.showMessageDialog(null, "將進入新增窗體！");

				JFrame jf2 = new JFrame("新增畫面");
				jf2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf2.setBounds(500, 500, 500, 500); // 設置窗口大小和位置
				JPanel panel2 = new JPanel();
				JLabel jl = new JLabel("幣別" + "     " + "幣別中文名稱" + "                " + "匯率"
						+ "                                " + "更新時間");
				jl.setBounds(20, 20, 400, 40);
				jf2.setContentPane(panel2); // 設定MyFrame視窗的容器為contentPane
				panel2.setLayout(null); // 設定contentPane物件不使用版面配置管理者

				panel2.add(jl);

				JTextField textFied1 = new JTextField(3);
				JTextField textFied2 = new JTextField(10);
				JTextField textFied3 = new JTextField(10);

				textFied1.setBounds(20, 60, 40, 20);
				textFied2.setBounds(100, 60, 40, 20);
				textFied3.setBounds(180, 60, 70, 20);
				panel2.add(textFied1);
				panel2.add(textFied2);
				panel2.add(textFied3);

				JButton btninsert = new JButton("新增");
				btninsert.setBounds(200, 150, 100, 40);
				btninsert.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Date date = new Date();
						SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
						// 寫入h2 db
						TB_COINDESK tTB_COINDESK = new TB_COINDESK();
						tTB_COINDESK.setCURCD(StringUtils.leftPad(textFied1.getText(), 3));
						tTB_COINDESK.setNAME(StringUtils.leftPad(textFied2.getText(), 16));
						tTB_COINDESK.setRATE(StringUtils.leftPad(textFied3.getText(), 11));
						tTB_COINDESK.setCREATEDATE(ft.format(date));
						tTB_COINDESK.setLASTUPDATEDATE(ft.format(date));

						// 先不做檢核
						tb_coindeskrepository.save(tTB_COINDESK);
						// 關閉視窗
						jf2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
				});

				panel2.add(btninsert);
				jf2.setVisible(true);
				jf2.setResizable(false); // 設定登入視窗不能調整大小
			}
		});
		panel.add(btn3);

		JButton btn4 = new JButton("修改");
		btn4.setBounds(20, 250, 100, 23);

		btn4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 開啟新視窗顯示輸入修改的db的資料 輸入完成關閉
				JOptionPane.showMessageDialog(null, "將進入修改窗體！");

				JFrame jf3 = new JFrame("修改畫面");
				jf3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf3.setBounds(500, 500, 500, 500); // 設置窗口大小和位置
				JPanel panel3 = new JPanel();
				JLabel jl = new JLabel("幣別" + "     " + "幣別中文名稱" + "                " + "匯率"
						+ "                                " + "更新時間");
				jl.setBounds(20, 20, 400, 40);
				jf3.setContentPane(panel3); // 設定MyFrame視窗的容器為contentPane
				panel3.setLayout(null); // 設定contentPane物件不使用版面配置管理者

				panel3.add(jl);

				JTextField textFied1 = new JTextField(3);
				JTextField textFied2 = new JTextField(10);
				JTextField textFied3 = new JTextField(10);

				textFied1.setBounds(20, 60, 40, 20);
				textFied2.setBounds(100, 60, 40, 20);
				textFied3.setBounds(180, 60, 70, 20);
				panel3.add(textFied1);
				panel3.add(textFied2);
				panel3.add(textFied3);

				JButton btninsert = new JButton("修改");
				btninsert.setBounds(200, 150, 100, 40);
				btninsert.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						TB_COINDESK tTB_COINDESK = new TB_COINDESK();
//						TB_COINDESK tTB_COINDESK = (TB_COINDESK) tb_coindeskrepository
//								.getReferenceById(textFied1.getText());

						Date date = new Date();
						SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
						tTB_COINDESK.setCURCD(StringUtils.leftPad(textFied1.getText(), 3));
						tTB_COINDESK.setNAME(StringUtils.leftPad(textFied2.getText(), 16));
						tTB_COINDESK.setRATE(StringUtils.leftPad(textFied3.getText(), 11));
						tTB_COINDESK.setCREATEDATE(ft.format(date));
						tTB_COINDESK.setLASTUPDATEDATE(ft.format(date));
						tb_coindeskrepository.save(tTB_COINDESK);

						jf3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
				});

				panel3.add(btninsert);
				jf3.setVisible(true);
				jf3.setResizable(false); // 設定登入視窗不能調整大小
			}
		});

		panel.add(btn4);

		JButton btn5 = new JButton("刪除");
		btn5.setBounds(20, 300, 100, 23);

		btn5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<TB_COINDESK> lTB_COINDESK = new ArrayList<TB_COINDESK>();
				// 顯示目前所有資料
				lTB_COINDESK = tb_coindeskrepository.findAll();
				if (lTB_COINDESK != null && !lTB_COINDESK.isEmpty()) {
					JOptionPane.showMessageDialog(null, "將進入刪除窗體！");
					JFrame jf4 = new JFrame("刪除畫面");
					jf4.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					jf4.setBounds(500, 500, 500, 500); // 設置窗口大小和位置
					JPanel panel4 = new JPanel();
					String inf = "";
					String cd = "";
					String time = "";
					JLabel jl = new JLabel("幣別" + "     " + "幣別中文名稱" + "                " + "匯率"
							+ "                                " + "更新時間");
					jl.setBounds(20, 20, 400, 40);
					panel4.add(jl);

					int i = 20;
					for (TB_COINDESK t : lTB_COINDESK) {
						cd = StringUtils.leftPad(matchCurcd(t.getCURCD()), 16);
						time = t.getLASTUPDATEDATE();
						inf = inf + StringUtils.leftPad(t.getCURCD(), 3) + "     " + cd + "                "
								+ StringUtils.leftPad(t.getRATE(), 11) + "         " + time;

						JLabel jl2 = new JLabel(inf);
						inf = "";
						jl2.setBounds(20, i + 20, 400, 40);
						i = i + 20; // 輸出位置
						panel4.add(jl2);
					}

					JButton btninsert = new JButton("刪除");
					btninsert.setBounds(200, 150, 100, 40);
					btninsert.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// 沒想好要怎麼刪除單一
							tb_coindeskrepository.deleteAll();
						}
					});
					panel4.add(btninsert);

					jf4.setContentPane(panel4); // 設定MyFrame視窗的容器為contentPane
					panel4.setLayout(null); // 設定contentPane物件不使用版面配置管理者

					jf4.setVisible(true);
					jf4.setResizable(false); // 設定登入視窗不能調整大小

				} else {
					JOptionPane.showMessageDialog(jf, "查無資料");
				}

			}
		});
		panel.add(btn5);

		jf.setVisible(true); // 視窗可見
		jf.setResizable(false); // 設定登入視窗不能調整大小

//		1. 幣別DB維護功能。
//		2. 呼叫coindesk的API。
//		3. 呼叫coindesk的API，並進行資料轉換，組成新API。 此新API提供： A. 更新時間（時間格式範例：1990/01/01 00:00:00）。 B. 幣別相關資訊（幣別，幣別中文名稱，以及匯率）。

//		1. 測試呼叫查詢幣別對應表資料API，並顯示其內容。
//		2. 測試呼叫新增幣別對應表資料API。
//		3. 測試呼叫更新幣別對應表資料API，並顯示其內容。
//		4. 測試呼叫刪除幣別對應表資料API。
//		5. 測試呼叫coindesk API，並顯示其內容。
//		6. 測試呼叫資料轉換的API，並顯示其內容。
	}

	class data {

		private String chartName;
		private HashMap<String, bpilogin> bpi;
		private timelogin time;
		private String disclaimer;

		public String getChartName() {
			return chartName;
		}

		public void setChartName(String chartName) {
			this.chartName = chartName;
		}

		public String getDisclaimer() {
			return disclaimer;
		}

		public void setDisclaimer(String disclaimer) {
			this.disclaimer = disclaimer;
		}

		public HashMap<String, bpilogin> getBpi() {
			return bpi;
		}

		public void setBpi(HashMap<String, bpilogin> bpi) {
			this.bpi = bpi;
		}

		public timelogin getTime() {
			return time;
		}

		public void setTime(timelogin time) {
			this.time = time;
		}

		class bpilogin {
			private String symbol;
			private String rate_float;
			private String code;
			private String rate;
			private String description;

			public String getSymbol() {
				return symbol;
			}

			public void setSymbol(String symbol) {
				this.symbol = symbol;
			}

			public String getRate_float() {
				return rate_float;
			}

			public void setRate_float(String rate_float) {
				this.rate_float = rate_float;
			}

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getRate() {
				return rate;
			}

			public void setRate(String rate) {
				this.rate = rate;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}
		}

		class timelogin {
			private String updateduk;
			private String updatedISO;
			private String updated;

			public String getUpdateduk() {
				return updateduk;
			}

			public void setUpdateduk(String updateduk) {
				this.updateduk = updateduk;
			}

			public String getUpdatedISO() {
				return updatedISO;
			}

			public void setUpdatedISO(String updatedISO) {
				this.updatedISO = updatedISO;
			}

			public String getUpdated() {
				return updated;
			}

			public void setUpdated(String updated) {
				this.updated = updated;
			}

		}
	}

	public static String matchCurcd(String curcd) {
		String name = "";

		switch (curcd) {
		case "TWD":
			name = "台幣";
			break;
		case "USD":
			name = "美金";
			break;
		case "GBP":
			name = "英鎊";
			break;
		case "EUR":
			name = "歐元";
			break;
		default:
			break;
		}

		return name;
	}

	// get
	public String getMethod(String url) throws IOException {
		URL restURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();

		conn.setRequestMethod("GET"); // POST GET PUT DELETE
		conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String rline = "";
		while ((line = br.readLine()) != null) {
//			System.out.println(line);
			rline += line;
		}

		br.close();

		return rline;
	}

	// post
	public void postMethod(String url, String query) throws IOException {
		URL restURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		PrintStream ps = new PrintStream(conn.getOutputStream());
		ps.print(query);
		ps.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}

		br.close();
	}

}
