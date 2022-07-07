package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/TB_COINDESKs")
public class TB_COINDESKContoroller {

//	@Resource
//	private TB_COINDESKService tb_coindeskservice;
//
//	// get全部
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	@ResponseBody
//	public List<TB_COINDESK> getAll() {
//		return tb_coindeskservice.findAll();
//	}
//
//	// get單個
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public TB_COINDESK getOne(@PathVariable String id) {
//		System.out.println("got parameter id = " + id);
//		return tb_coindeskservice.findOne(id);
//	}
//
//	// insert新增
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	@ResponseBody
//	public TB_COINDESK insert(TB_COINDESK coindesk) {
//		System.out.println("got parameter TB_COINDESK = " + coindesk.toString());
//		return tb_coindeskservice.Insert(coindesk);
//	}
//
//	// update更新
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	@ResponseBody
//	public TB_COINDESK update(TB_COINDESK coindesk) {
//		TB_COINDESK tTB_COINDESK = tb_coindeskservice.findOne(coindesk.getCURCD());
//		System.out.println("got parameter TB_COINDESK = " + coindesk.toString());
//		if (tTB_COINDESK != null) {
//			tTB_COINDESK.setNAME(coindesk.getNAME());
//			tTB_COINDESK.setRATE(coindesk.getRATE());
//			tTB_COINDESK.setLASTUPDATEDATE(coindesk.getLASTUPDATEDATE());
//			return tb_coindeskservice.update(tTB_COINDESK);
//		} else {
//			return null;
//		}
//
//	}
//
//	// delete 刪除
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	@ResponseBody
//	public void delete(TB_COINDESK coindesk) {
//		TB_COINDESK tTB_COINDESK = tb_coindeskservice.findOne(coindesk.getCURCD());
//		System.out.println("got parameter TB_COINDESK = " + coindesk.toString());
//		if (tTB_COINDESK != null) {
//
//			tb_coindeskservice.delete(tTB_COINDESK);
//		}
//	}

}
