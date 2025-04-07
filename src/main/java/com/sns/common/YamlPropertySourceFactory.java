package com.sns.common;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.Properties;

/**
 * YamlPropertySourceFactory는 YAML 파일을 읽어 Spring의 PropertySource로 변환하기 위한 Factory 클래스입니다.
 * 이 클래스를 사용하면 YAML 형식의 프로퍼티 파일을 @PropertySource 어노테이션과 함께 사용할 수 있습니다.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

	/**
	 * 주어진 EncodedResource (YAML 파일)를 읽어 Properties 객체로 변환한 후,
	 * 이를 PropertiesPropertySource로 감싸서 반환합니다.
	 *
	 * @param name             프로퍼티 소스의 이름 (null일 수 있음)
	 * @param encodedResource  YAML 파일을 포함하는 EncodedResource 객체
	 * @return YAML 파일로부터 생성된 PropertiesPropertySource 객체
	 */
	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) {
		// YAML 파일을 Properties 객체로 변환하기 위한 YamlPropertiesFactoryBean 생성
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		// EncodedResource로부터 실제 리소스를 설정
		factory.setResources(encodedResource.getResource());

		// YAML 파일에서 Properties 객체를 생성
		Properties properties = factory.getObject();

		// 생성된 properties 객체가 null이 아님을 보장
		assert properties != null;
		// 파일 이름을 PropertySource의 이름으로 사용하여 PropertiesPropertySource 객체를 생성 후 반환
		return new PropertiesPropertySource(
				Objects.requireNonNull(encodedResource.getResource().getFilename()),
				properties
		);
	}
}
