import {
    AppstoreOutlined,
    BarChartOutlined,
    CloudOutlined,
    ShopOutlined,
    TeamOutlined,
    UploadOutlined,
    UserOutlined,
    VideoCameraOutlined
} from '@ant-design/icons';
import { Flex, Layout, Menu, MenuProps, theme, Typography } from 'antd';
import React from 'react';

const { Title } = Typography;

const { Header, Sider, Content } = Layout;

const MainLayout: React.FC = () => {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const items: MenuProps['items'] = [
        UserOutlined,
        VideoCameraOutlined,
        UploadOutlined,
        BarChartOutlined,
        CloudOutlined,
        AppstoreOutlined,
        TeamOutlined,
        ShopOutlined,
        UserOutlined,
        VideoCameraOutlined,
        UploadOutlined,
        BarChartOutlined,
        CloudOutlined,
        AppstoreOutlined,
        TeamOutlined,
        ShopOutlined,
        UserOutlined,
        VideoCameraOutlined,
        UploadOutlined,
        BarChartOutlined,
        CloudOutlined,
        AppstoreOutlined,
        TeamOutlined,
        ShopOutlined,
    ].map((icon, index) => ({
        key: String(index + 1),
        icon: React.createElement(icon),
        label: `nav ${index + 1}`,
    }));

    return (
        <Flex vertical justify='center' align='center'
            style={{
                backgroundColor: colorBgContainer,
                height: '100vh',
                width: '100vw',
            }}>
            <Header style={{
                padding: 0,
                background: colorBgContainer,
                position: 'sticky',
                top: 0,
                zIndex: 1,
                width: '100%',
                display: 'flex',
                alignItems: 'center',
            }}>
                <Flex justify='flex-start' align='center'>
                    <Title level={3} style={{ margin: 16 }} >CarSync</Title>
                </Flex>
            </Header>
            <Layout style={{
                overflow: 'auto',
                width: '100%',
                position: 'sticky',
                padding: 24,
                insetInlineStart: 0,
                top: 0,
                bottom: 0,
                scrollbarWidth: 'thin',
                scrollbarGutter: 'stable',
                borderRight: 0,
            }}>
                <Sider style={{
                    backgroundColor: 'white',
                    margin: 0,
                    height: '100%',
                    overflow: 'auto',
                    position: 'sticky',
                    insetInlineStart: 0,
                    background: colorBgContainer,
                    borderTopLeftRadius: borderRadiusLG,
                    borderBottomLeftRadius: borderRadiusLG,
                    borderTopRightRadius: 0,
                    borderBottomRightRadius: 0,
                    border: 0,
                    top: 0,
                    bottom: 0,
                    scrollbarWidth: 'thin',
                    scrollbarGutter: 'stable',
                }}>
                    <Menu
                        mode="inline"
                        items={items}
                    />
                </Sider>
                <Layout>
                    <Content style={{
                        backgroundColor: 'white',
                        margin: 0,
                        height: '100%',
                        overflow: 'auto',
                        position: 'sticky',
                        insetInlineStart: 0,
                        background: colorBgContainer,
                        borderTopLeftRadius: 0,
                        borderBottomLeftRadius: 0,
                        borderTopRightRadius: borderRadiusLG,
                        borderBottomRightRadius: borderRadiusLG,
                        border: 0,
                        top: 0,
                        bottom: 0,
                        scrollbarWidth: 'thin',
                        scrollbarGutter: 'stable',
                    }}>
                        <div
                            style={{
                                padding: 0,
                                textAlign: 'center',
                                background: colorBgContainer,
                                borderRadius: borderRadiusLG,
                            }}
                        >
                            <p>long content</p>
                            {
                                // indicates very long content
                                Array.from({ length: 100 }, (_, index) => (
                                    <React.Fragment key={index}>
                                        {index % 20 === 0 && index ? 'more' : '...'}
                                        <br />
                                    </React.Fragment>
                                ))
                            }
                        </div>
                    </Content>
                </Layout>
            </Layout>
        </Flex>
    );
};

export default MainLayout;