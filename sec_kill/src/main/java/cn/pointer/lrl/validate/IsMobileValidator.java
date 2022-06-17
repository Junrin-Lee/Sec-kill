package cn.pointer.lrl.validate;

import cn.pointer.lrl.annotation.IsMobile;
import cn.pointer.lrl.utils.ValidateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    // 默认注解下是不必须的
    private boolean require = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        // 初始化时获取注解里面的方法值
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 由于注解有require方法，说明注解一定存在内容，因此直接进行判断
        if (require) {
            return ValidateUtil.isMobile(s);
        } else {
            // 首先需要手动判断注解非空
            if (StringUtils.isEmpty(s)) {
                return true;
            } else {
                return ValidateUtil.isMobile(s);
            }
        }
    }
}
