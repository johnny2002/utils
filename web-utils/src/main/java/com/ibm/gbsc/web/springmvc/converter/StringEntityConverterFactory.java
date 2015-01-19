package com.ibm.gbsc.web.springmvc.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.ibm.gbsc.utils.vo.BaseVO;

/**
 * 转换从页面来的String主键，到服务端的java中实体.
 * 如页面选择了用户"admin"，java模型里是User类型，这个转换器负责把"admin"转换成User类的实例
 *
 * @author Johnny
 *
 */
public class StringEntityConverterFactory implements ConverterFactory<String, BaseVO> {
	@Autowired
	BaseVORetriever voRetriever;

	/** {@inheritDoc} */
	@Override
	public <T extends BaseVO> Converter<String, T> getConverter(Class<T> targetType) {

		return new StringEntityConverter<T>(voRetriever, targetType);
	}

	static final class StringEntityConverter<T extends BaseVO> implements Converter<String, T> {
		private final BaseVORetriever service;
		private final Class<T> targetType;

		/**
		 * @param service
		 *            service.
		 * @param targetType
		 *            targetType.
		 */
		public StringEntityConverter(BaseVORetriever service, Class<T> targetType) {
			super();
			this.service = service;
			this.targetType = targetType;
		}

		/** {@inheritDoc} */
		@Override
		public T convert(String source) {
			return service.getEntityById(targetType, source);
		}

	}

}
