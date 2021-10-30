import React from 'react';
import clsx from 'clsx';
import styles from './HomepageFeatures.module.css';

const FeatureList = [
  {
    title: 'Easy to Use',
    Svg: require('../../static/img/undraw_docusaurus_mountain.svg').default,
    description: (
      <>
        Import the modules that you need and start transforming your types.
      </>
    ),
  },
  {
    title: 'Type safety',
    Svg: require('../../static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        Let the compiler work for you, safety conversions,
          get compile errors if a transformation is missing something.
      </>
    ),
  },
  {
    title: 'Custom transformations',
    Svg: require('../../static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
          Convert automatically to camelCase, snake_case, add suffixes or decide your custom function,
          make custom transformation based on types easily.
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} alt={title} />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
