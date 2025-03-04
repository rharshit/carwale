import {
    AppstoreOutlined,
    BarsOutlined,
    CarOutlined,
    CheckCircleOutlined,
    CheckSquareOutlined,
    CompressOutlined,
    DashboardOutlined,
    DotChartOutlined,
    ExpandOutlined,
    FileSyncOutlined,
    FormatPainterOutlined,
    HomeOutlined,
    MoonOutlined,
    SunOutlined
} from '@ant-design/icons';
import { ConfigProvider, Flex, FloatButton, Layout, Menu, MenuProps, theme, Typography } from 'antd';
import React, { useEffect } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import carSyncLogo from '../assets/CarSync.svg';
import CarWalePage from './fetch/CarWalePage';
import HomePage from './home/HomePage';
import ListAllPage from './view-data/list-all/ListAllPage';
import ShowSpecsPage from './view-data/show-specs/ShowSpecsPage';
import ViewGraphPage from './view-data/view-graph/ViewGraphPage';


const { Title } = Typography;

const { Header, Sider } = Layout;

type MainLayoutProps = {
    isDark: boolean,
    setDark: React.Dispatch<React.SetStateAction<boolean>>,
    isCompact: boolean,
    setCompact: React.Dispatch<React.SetStateAction<boolean>>,
    isRounded: boolean,
    setRounded: React.Dispatch<React.SetStateAction<boolean>>
}

function MainLayout(mainLayoutProps: MainLayoutProps) {
    const { isDark, setDark, isCompact, setCompact, isRounded, setRounded } = mainLayoutProps
    const navigate = useNavigate();

    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const navItems: MenuProps['items'] = [
        {
            key: 'home',
            icon: <HomeOutlined />,
            label: 'Home',
            onClick: () => navigate('/home')
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
                    onClick: () => navigate('/view-data/list-all')
                },
                {
                    key: 'view-data/view-graph',
                    label: 'View graph',
                    icon: <DotChartOutlined />,
                    onClick: () => navigate('/view-data/view-graph')
                },
                {
                    key: 'view-data/show-specs',
                    label: 'Show specs',
                    icon: <DashboardOutlined />,
                    onClick: () => navigate('/view-data/show-specs')
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
                    icon: <CarOutlined />,
                    onClick: () => navigate('/fetch-data/carwale')
                }
            ]
        }
    ]

    const [collapsedSidebar, setCollapsedSideBar] = React.useState<boolean>(true);
    const [openKeys, setOpenKeys] = React.useState<string[]>([]);
    const [selectedKeys, setSelectedKeys] = React.useState<string[]>([]);
    const rootSubmenuKeys = navItems.map(item => item?.key);

    useEffect(() => {
        const path = window.location.pathname;
        updateSelectedKeys([path.startsWith('/') ? path.substring(1) : path], true);
    }, []);

    const onOpenChange: MenuProps['onOpenChange'] = (keys) => {
        updateOpenKeys(keys, false);
    };

    const updateOpenKeys = (keys: string[], initalLoad: boolean) => {
        if (!initalLoad && collapsedSidebar) {
            return;
        }
        if (keys.length === 0) {
            setOpenKeys([]);
            return;
        }
        const latestOpenKey = keys.find(key => openKeys.indexOf(key) === -1)?.split('/')[0];
        if (rootSubmenuKeys.indexOf(latestOpenKey!) === -1) {
            setOpenKeys(keys);
        } else {
            setOpenKeys(latestOpenKey ? [latestOpenKey] : []);
        }
    };

    const updateSelectedKeys = (keys: string[], initalLoad: boolean) => {
        setSelectedKeys(keys);
        updateOpenKeys(keys, initalLoad);
    }

    return (
        <Flex vertical justify='center' align='center'
            style={{
                height: '100vh',
                width: '100vw',
            }}>
            <Layout style={{ width: '100%', padding: 24 }}>
                <Header style={{
                    padding: 0,
                    background: colorBgContainer,
                    borderRadius: `${borderRadiusLG}px ${borderRadiusLG}px ${0}px ${0}px`,
                    position: 'sticky',
                    top: 0,
                    zIndex: 1,
                    display: 'flex',
                    alignItems: 'center',
                }}>
                    <Flex justify='flex-start' align='center'>
                        <Flex gap={16}>
                            <img src={carSyncLogo} className="logo" alt="CarSync logo" width={32} height={32} style={{ margin: '16px 0 16px 16px' }} />
                            <Title level={3} style={{ margin: '16px 16px 16px 0' }} >CarSync</Title>
                        </Flex>
                    </Flex>
                </Header>
                <Layout style={{
                    overflow: 'auto',
                    width: '100%',
                    position: 'sticky',
                    insetInlineStart: 0,
                    top: 0,
                    bottom: 0,
                    scrollbarWidth: 'thin',
                    scrollbarGutter: 'stable',
                    borderRight: 0,
                }}>
                    <Sider
                        trigger={null}
                        collapsible
                        collapsed={collapsedSidebar}
                        style={{
                            backgroundColor: 'white',
                            margin: 0,
                            height: '100%',
                            overflow: 'auto',
                            position: 'sticky',
                            insetInlineStart: 0,
                            background: colorBgContainer,
                            borderTopLeftRadius: 0,
                            borderBottomLeftRadius: borderRadiusLG,
                            borderTopRightRadius: 0,
                            borderBottomRightRadius: 0,
                            border: 0,
                            top: 0,
                            bottom: 0,
                            scrollbarWidth: 'thin',
                            scrollbarGutter: 'stable',
                        }}>
                        <ConfigProvider theme={{
                            components: {
                                Menu: {
                                    itemActiveBg: '#0000',
                                    itemSelectedBg: '#0000',
                                    itemHoverBg: '#0000',
                                    itemHoverColor: '#667d99',
                                    itemColor: '#808080',
                                },
                            },
                        }}>
                            <Menu
                                mode="inline"
                                inlineCollapsed={collapsedSidebar}
                                triggerSubMenuAction='click'
                                items={navItems}
                                selectedKeys={selectedKeys}
                                openKeys={!collapsedSidebar ? openKeys : []}
                                onOpenChange={onOpenChange}
                                onSelect={({ key }) => updateSelectedKeys([key], false)}
                                onMouseEnter={() => { setCollapsedSideBar(false) }}
                                onMouseLeave={() => { setCollapsedSideBar(true) }}
                                style={{
                                    padding: '16px 0px 16px 0px',
                                    border: 0,
                                }}
                            />
                        </ConfigProvider>
                    </Sider>
                    <Layout style={{
                        backgroundColor: 'white',
                        margin: 0,
                        height: '100%',
                        padding: collapsedSidebar ? '0px 24px 0px 0px' : '0px 24px 0px 24px',
                        overflow: 'auto',
                        position: 'sticky',
                        insetInlineStart: 0,
                        background: colorBgContainer,
                        borderTopLeftRadius: 0,
                        borderBottomLeftRadius: 0,
                        borderTopRightRadius: 0,
                        borderBottomRightRadius: borderRadiusLG,
                        border: 0,
                        top: 0,
                        bottom: 0,
                        scrollbarWidth: 'none',
                        scrollbarGutter: 'stable',
                    }}>
                        <Routes>
                            <Route path='/' element={<HomePage />} />
                            <Route path='/home' element={<HomePage />} />
                            <Route path='/view-data/list-all' element={<ListAllPage />} />
                            <Route path='/view-data/view-graph' element={<ViewGraphPage />} />
                            <Route path='/view-data/show-specs' element={<ShowSpecsPage />} />
                            <Route path='/fetch-data/carwale' element={<CarWalePage />} />
                        </Routes>
                    </Layout>
                </Layout>
            </Layout>
            <ConfigProvider theme={{
                token: {
                    boxShadowSecondary: ''
                }
            }}>
                <FloatButton.Group
                    trigger='click'
                    shape={isRounded ? 'circle' : 'square'}
                    icon={<FormatPainterOutlined />}
                    style={{ insetInlineEnd: 24, bottom: 24 }}
                >
                    <FloatButton icon={isDark ? <SunOutlined /> : <MoonOutlined />} onClick={() => setDark(!isDark)} />
                    <FloatButton icon={isCompact ? <ExpandOutlined /> : <CompressOutlined />} onClick={() => setCompact(!isCompact)} />
                    <FloatButton icon={isRounded ? <CheckSquareOutlined /> : <CheckCircleOutlined />} onClick={() => setRounded(!isRounded)} />
                </FloatButton.Group>
            </ConfigProvider>

        </Flex>
    );
};

export default MainLayout;