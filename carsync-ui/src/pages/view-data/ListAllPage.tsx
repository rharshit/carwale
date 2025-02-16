import { Flex, Typography } from 'antd';
import React from 'react';
import { FilterComponent } from './FilterComponent';

const { Title } = Typography;

const ListAllPage: React.FC = () => {
    return (
        <>
            <Flex vertical>
                <Title level={3}>
                    All Cars
                </Title>
                <FilterComponent />
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