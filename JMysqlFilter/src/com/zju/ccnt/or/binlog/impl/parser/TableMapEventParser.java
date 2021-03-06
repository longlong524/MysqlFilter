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
import com.zju.ccnt.or.binlog.impl.event.TableMapEvent;
import com.zju.ccnt.or.common.glossary.Metadata;
import com.zju.ccnt.or.net.XInputStream;

/**
 * 
 * @author Xianglong Yao
 */
public class TableMapEventParser extends AbstractBinlogEventParser {
	//
	private boolean reusePreviousEvent = true;

	/**
	 * 
	 */
	public TableMapEventParser() {
		super(TableMapEvent.EVENT_TYPE);
	}

	/**
	 * 
	 */
	public boolean isReusePreviousEvent() {
		return reusePreviousEvent;
	}

	public void setReusePreviousEvent(boolean reusePreviousEvent) {
		this.reusePreviousEvent = reusePreviousEvent;
	}

	/**
	 * @throws Exception
	 * 
	 */
	@Override
	public AbstractBinlogEventV4 parse(XInputStream is,
			BinlogEventV4Header header, BinlogParserContext context)
			throws Exception {
		final long tableId = is.readLong(6);
		if (this.reusePreviousEvent
				&& context.getTableMapEvent(tableId) != null) {
			is.skip(is.available());
			final TableMapEvent event = context.getTableMapEvent(tableId)
					.copy();
			event.setHeader(header);
			return event;
		}

		//
		final TableMapEvent event = new TableMapEvent(header);
		event.setTableId(tableId);
		event.setReserved(is.readInt(2));
		event.setDatabaseNameLength(is.readInt(1));
		event.setDatabaseName(is.readNullTerminatedString());
		event.setTableNameLength(is.readInt(1));
		event.setTableName(is.readNullTerminatedString());
		event.setColumnCount(is.readUnsignedLong());
		event.setColumnTypes(is.readBytes(event.getColumnCount().intValue()));
		event.setColumnMetadataCount(is.readUnsignedLong());
		event.setColumnMetadata(Metadata.valueOf(event.getColumnTypes(),
				is.readBytes(event.getColumnMetadataCount().intValue())));
		event.setColumnNullabilities(is.readBit(event.getColumnCount()
				.intValue(), true));
		return event;
	}
}
