import { Flex, Typography } from 'antd';
import React from 'react';
import { post } from '../../service/api';
import { CarFilter, FilterComponent } from './FilterComponent';

const { Title } = Typography;

const ListAllPage: React.FC = () => {

    const onApplyFilter = async (carFilter: CarFilter) => {
        console.log('carFilter', carFilter);
        Promise.resolve(post('/car', carFilter).then(res => {
            console.log('data', res)
        }))
        console.log('onApplyFilter')
    }

    return (
        <>
            <Flex vertical>
                <Title level={3}>
                    All Cars
                </Title>
                <FilterComponent
                    onApplyFilter={onApplyFilter}
                />
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