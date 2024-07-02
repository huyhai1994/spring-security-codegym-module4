package com.codegym.security;

import com.codegym.controller.CustomSuccessHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*TODO: Bước 3: Bổ sung cấu hình security configuration
        Bước 2: Cài đặt cấu hình security của dự án
        - Xây dựng lớp SecurityConfiguration,
        kế thừa WebSecurityConfigurerAdapter và đánh dấu với @EnableWebSecurity*/
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    /*TODO: lop WebSecurityConfigurerAdapter
     *  Lớp này tạo ra một bộ lọc Servlet được gọi là SpringSecurityFilterChain,
     *  nhắm bảo vệ các request được khai báo tương ứng trong ứng dụng.
     *  */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*TODO: Phương thức configure(AuthenticationManagerBuilder auth):
           xác thực thông tin đăng nhập của người dùng.*/
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}12345").roles("USER")
                .and()
                .withUser("admin").password("{noop}12345").roles("ADMIN")
                .and()
                .withUser("dba").password("{noop}12345").roles("ADMIN", "DBA");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*TODO: Phương thức configure(HttpSecurity http): cấu hình bảo mật dựa
           trên các yêu cầu HTTP. Mặc định các yêu cầu đều được bảo mật
            nhưng chúng ta có thể tự config cho các yêu cầu của dự án.*/
        /*TODO: authorizeHttpRequests(): bảo mật tất các request đến dự án.
        *       requestMatchers("/url"): chỉ định rõ request được bảo vệ theo URI.
                    permitAll(): các request không cần bảo mật,
                                  có thể truy cập bất kỳ lúc nào.
                    hasRole("Role"): các request được bảo mật và
                                    chỉ được truy cập nếu người dùng yêu cầu có role tương ứng.
                formLogin(): sử dụng form login mặc đinh.
                            Bạn có thể tự tạo và đưa vào view login riêng của dự án.
                logout(): hành động xóa authentication đã xác nhận trước đó.
                    logoutRequestMatcher(): chỉ định đường link để thực thi hành động logout.
                 successHandler(new CustomSuccessHandler()):
                        dùng để điều hướng trang web sau khi đăng nhập thành công.
                exceptionHandling(): xử lý khi có phát sinh lỗi.
                accessDeniedPage("/viewname"): điều hướng request có lỗi đến trang view tương ứng.
*/
        http.authorizeHttpRequests()
                .requestMatchers("/", "/home").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/dba/**").hasAnyRole("ADMIN", "DBA")
                .and()
                .formLogin().successHandler(new CustomSuccessHandler())
                .usernameParameter("ssoId").passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().exceptionHandling().accessDeniedPage("/accessDenied");
    }
}
