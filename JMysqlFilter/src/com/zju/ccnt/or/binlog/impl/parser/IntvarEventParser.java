/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zju.ccnt.or.binlog.impl.parser;

import com.zju.ccnt.or.binlog.BinlogEventV4Header;
import com.zju.ccnt.or.binlog.BinlogParserContext;
import com.zju.ccnt.or.binlog.impl.event.AbstractBinlogEventV4;
import com.zju.ccnt.or.binlog.impl.event.IntvarEvent;
import com.zju.ccnt.or.common.glossary.UnsignedLong;
import com.zju.ccnt.or.net.XInputStream;

/**
 * 
 * @author Xianglong Yao
 */
public class IntvarEventParser extends AbstractBinlogEventParser {

	/**
	 * 
	 */
	public IntvarEventParser() {
		super(IntvarEvent.EVENT_TYPE);
	}

	/**
	 * @throws Exception 
	 * 
	 */
	@Override
	public AbstractBinlogEventV4 parse(XInputStream is, 
			BinlogEventV4Header header,BinlogParserContext context)
	throws Exception {
		final IntvarEvent event = new IntvarEvent(header);
		event.setType(is.readInt(1));
		event.setValue(UnsignedLong.valueOf(is.readLong(8)));
		return event;
	}
}
