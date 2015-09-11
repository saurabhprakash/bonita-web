package org.bonitasoft.livingapps.menu;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.business.application.ApplicationMenu;
import org.bonitasoft.engine.business.application.impl.ApplicationMenuImpl;
import org.bonitasoft.engine.business.application.impl.ApplicationPageImpl;
import org.bonitasoft.livingapps.menu.MenuFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuFactoryTest {

    ApplicationMenuImpl aMenuLink;

    ApplicationMenuImpl aMenuContainer;

    ApplicationMenuImpl aNestedMenuLink;

    ApplicationPageImpl aPage = new ApplicationPageImpl(1L, 2L, "token");

    @Mock(answer = Answers.RETURNS_MOCKS)
    ApplicationAPI applicationApi;

    @Before
    public void beforeEach() throws Exception {
        aMenuLink = new ApplicationMenuImpl("link", 1L, 2L, 1);
        aMenuLink.setId(1L);
        aMenuContainer = new ApplicationMenuImpl("container", 1L, null, 2);
        aMenuContainer.setId(2L);
        aNestedMenuLink = new ApplicationMenuImpl("nested-link", 1L, 2L, 2);
        aNestedMenuLink.setId(3L);
        aNestedMenuLink.setParentId(2L);
        given(applicationApi.getApplicationPage(aMenuLink.getApplicationPageId())).willReturn(aPage);
    }

    @Test
    public void should_create_a_MenuLink_with_the_page_token_it_is_pointing_at_when_menu_is_a_link() throws Exception {
        MenuFactory factory = new MenuFactory(applicationApi);

        assertThat(factory.create(asList((ApplicationMenu) aMenuLink)).get(0).getHtml())
                .isEqualTo("<li><a href=\"token\">link</a></li>");
    }

    @Test
    public void should_create_an_empty_MenuContainer() throws Exception {
        MenuFactory factory = new MenuFactory(applicationApi);

        assertThat(factory.create(asList((ApplicationMenu) aMenuContainer)).get(0).getHtml())
                .isEqualTo(new StringBuilder()
                        .append("<li class=\"dropdown\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">container <span class=\"caret\"></span></a>")
                        .append("<ul class=\"dropdown-menu\" role=\"menu\">")
                        .append("</ul></li>").toString());
    }

    @Test
    public void should_create_a_MenuContainer_containing_a_MenuLink() throws Exception {
        MenuFactory factory = new MenuFactory(applicationApi);

        assertThat(factory.create(asList((ApplicationMenu) aMenuContainer, aNestedMenuLink)).get(0).getHtml())
                .isEqualTo(new StringBuilder()
                        .append("<li class=\"dropdown\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">container <span class=\"caret\"></span></a>")
                        .append("<ul class=\"dropdown-menu\" role=\"menu\">")
                        .append("<li><a href=\"token\">nested-link</a></li>")
                        .append("</ul></li>").toString());
    }
}
