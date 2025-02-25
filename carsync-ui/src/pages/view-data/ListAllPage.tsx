import { Flex, Typography, theme } from 'antd';
import React, { useEffect, useState } from 'react';
import { post } from '../../service/api';
import { CarFilter, FilterSortComponent } from './filter/FilterComponent';

const { Title } = Typography;

const ListAllPage: React.FC = () => {

    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const [carFilter, setCarFilter] = useState<CarFilter>()

    useEffect(() => {
        fetchCars(carFilter)
    }, [carFilter])

    async function fetchCars(carFilter: CarFilter | undefined) {
        if (!carFilter) {
            return;
        }
        console.log('Fetching cars', carFilter)
        Promise.resolve(post('/car', carFilter).then(res => {
            console.log('data', res)
        }))
    }

    const onApplyFilter = (carFilter: CarFilter) => {
        setCarFilter(carFilter)
    }

    return (
        <>
            <Flex vertical>
                <Flex vertical style={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 1,
                    width: '100%',
                    display: 'flex',
                    background: colorBgContainer,
                }}>
                    <Title level={3}>
                        All Cars
                    </Title>
                    <FilterSortComponent
                        onApplyFilter={onApplyFilter}
                    />
                </Flex>
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
                <p>end</p>
            </Flex>
        </>
    );
}

export default ListAllPage;