export interface IImage {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
}

export class Image implements IImage {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any
    ) {}
}
