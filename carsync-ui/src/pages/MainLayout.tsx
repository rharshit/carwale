import {
    AppstoreOutlined,
    BarsOutlined,
    CarOutlined,
    DashboardOutlined,
    DotChartOutlined,
    FileSyncOutlined,
    HomeOutlined
} from '@ant-design/icons';
import { Flex, Layout, Menu, MenuProps, theme, Typography } from 'antd';
import React from 'react';

const { Title } = Typography;

const { Header, Sider } = Layout;

const MainLayout: React.FC = () => {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const navItems: MenuProps['items'] = [
        {
            key: 'home',
            icon: <HomeOutlined />,
            label: 'Home',
        },
        {
            key: 'view-data',
            icon: <AppstoreOutlined />,
            label: 'View Data',
            children: [
                {
                    key: 'view-data/list-all',
                    label: 'List all',
                    icon: <BarsOutlined />,
                },
                {
                    key: 'view-data/view-graph',
                    label: 'View graph',
                    icon: <DotChartOutlined />
                },
                {
                    key: 'view-data/show-specs',
                    label: 'Show specs',
                    icon: <DashboardOutlined />
                },
            ]
        },
        {
            key: 'fetch-data',
            icon: <FileSyncOutlined />,
            label: 'Fetch Data',
            children: [
                {
                    key: 'fetch-data/carwale',
                    label: 'CarWale',
                    icon: <CarOutlined />
                }
            ]
        }
    ]

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
                        items={navItems}
                        style={{
                            border: 0,
                        }}
                    />
                </Sider>
                <Layout style={{
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
                    <Title level={4} style={{ margin: 16 }} >Welcome to CarSync home</Title>
                </Layout>
            </Layout>
        </Flex>
    );
};

export default MainLayout;